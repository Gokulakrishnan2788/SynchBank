package com.architect.banking.feature.payments.data

import android.content.Context
import com.architect.banking.core.domain.Result
import com.architect.banking.feature.payments.domain.AddBeneficiaryResult
import com.architect.banking.feature.payments.domain.Beneficiary
import com.architect.banking.feature.payments.domain.BeneficiaryGrid
import com.architect.banking.feature.payments.domain.BeneficiaryStore
import com.architect.banking.feature.payments.domain.SourceAccount
import com.architect.banking.feature.payments.domain.TransferRepository
import com.architect.banking.feature.payments.domain.TransferResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class TransferRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val beneficiaryStore: BeneficiaryStore,
) : TransferRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private fun readAsset(path: String): String =
        context.assets.open(path).bufferedReader().use { it.readText() }

    override suspend fun getSourceAccounts(): Result<List<SourceAccount>> {
        return try {
            val raw = readAsset("mock/api/source_accounts.json")
            val root = json.parseToJsonElement(raw).jsonObject
            val data = root["data"]!!.jsonArray
            val accounts = data.map { el ->
                val obj = el.jsonObject
                SourceAccount(
                    id = obj["id"]!!.jsonPrimitive.content,
                    name = obj["name"]!!.jsonPrimitive.content,
                    maskedNumber = obj["maskedNumber"]!!.jsonPrimitive.content,
                    balance = obj["balance"]!!.jsonPrimitive.content,
                    balanceRaw = obj["balanceRaw"]!!.jsonPrimitive.double,
                )
            }
            Result.Success(accounts)
        } catch (e: Exception) {
            Result.Error("LOAD_ERROR", "Failed to load accounts: ${e.message}")
        }
    }

    override suspend fun getBeneficiaries(): Result<BeneficiaryGrid> {
        return Result.Success(BeneficiaryGrid(2, beneficiaryStore.getAll()))
    }

    override suspend fun submitTransfer(
        sourceAccountId: String,
        beneficiaryId: String,
        amount: Double,
        note: String?,
    ): Result<TransferResult> {
        return try {
            val raw = readAsset("mock/api/transfer_submit.json")
            val root = json.parseToJsonElement(raw).jsonObject
            val data = root["data"]!!.jsonObject
            Result.Success(
                TransferResult(
                    transferId = data["transferId"]!!.jsonPrimitive.content,
                    status = data["status"]!!.jsonPrimitive.content,
                    message = data["message"]!!.jsonPrimitive.content,
                    referenceNumber = data["referenceNumber"]?.jsonPrimitive?.content ?: "REF-001",
                ),
            )
        } catch (e: Exception) {
            Result.Error("SUBMIT_ERROR", "Transfer failed: ${e.message}")
        }
    }

    override suspend fun addBeneficiary(
        accountName: String,
        bankName: String,
        accountNumber: String,
        nickname: String?,
    ): Result<AddBeneficiaryResult> {
        return try {
            val raw = readAsset("mock/api/add_beneficiary.json")
            val root = json.parseToJsonElement(raw).jsonObject
            val data = root["data"]!!.jsonObject
            beneficiaryStore.add(accountName, bankName, accountNumber, nickname)
            Result.Success(
                AddBeneficiaryResult(
                    beneficiaryId = data["beneficiaryId"]!!.jsonPrimitive.content,
                    status = data["status"]!!.jsonPrimitive.content,
                    message = data["message"]!!.jsonPrimitive.content,
                ),
            )
        } catch (e: Exception) {
            Result.Error("SAVE_ERROR", "Failed to save beneficiary: ${e.message}")
        }
    }
}
