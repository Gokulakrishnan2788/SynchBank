package com.architect.banking.feature.payments

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.architect.banking.core.domain.BaseViewModel
import com.architect.banking.core.domain.Result
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.model.ScreenModel
import com.architect.banking.engine.sdui.model.SduiComponentType
import com.architect.banking.engine.sdui.parser.SDUIParser
import com.architect.banking.feature.payments.domain.Beneficiary
import com.architect.banking.feature.payments.domain.BeneficiaryStore
import com.architect.banking.feature.payments.domain.SubmitTransferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val sduiParser: SDUIParser,
    private val submitTransferUseCase: SubmitTransferUseCase,
    private val beneficiaryStore: BeneficiaryStore,
    @ApplicationContext private val context: Context,
) : BaseViewModel<TransferState, TransferIntent, TransferEffect>() {

    override fun initialState() = TransferState()

    init {
        handleIntent(TransferIntent.LoadScreen)
        // Live-update the beneficiary grid whenever the store changes
        viewModelScope.launch {
            beneficiaryStore.beneficiaries.collect { bens ->
                val model = state.value.screenModel ?: return@collect
                setState { copy(screenModel = model.withPatchedBeneficiaryGrid(bens)) }
            }
        }
    }

    override suspend fun reduce(intent: TransferIntent) {
        when (intent) {
            is TransferIntent.LoadScreen -> loadScreen()
            is TransferIntent.HandleAction -> onHandleAction(intent.actionId)
        }
    }

    private suspend fun loadScreen() {
        setState { copy(isLoading = true, error = null) }
        try {
            val json = context.assets
                .open("mock/screens/transfer_screen.json")
                .bufferedReader()
                .use { it.readText() }
            when (val result = sduiParser.parse(json)) {
                is Result.Success -> {
                    val patched = result.data.withPatchedBeneficiaryGrid(beneficiaryStore.getAll())
                    setState { copy(isLoading = false, screenModel = patched) }
                }
                is Result.Error -> setState { copy(isLoading = false, error = result.message) }
                is Result.Loading -> Unit
            }
        } catch (e: Exception) {
            setState { copy(isLoading = false, error = "Failed to load screen: ${e.message}") }
        }
    }

    private suspend fun onHandleAction(actionId: String) {
        when {
            actionId.startsWith("ACCOUNT_SELECTED:") -> {
                val accountId = actionId.removePrefix("ACCOUNT_SELECTED:")
                setState { copy(selectedAccountId = accountId) }
            }

            actionId.startsWith("AMOUNT_CHANGED:") -> {
                val amountStr = actionId.removePrefix("AMOUNT_CHANGED:")
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                setState { copy(amount = amount) }
            }

            actionId.startsWith("NOTE_CHANGED:") -> {
                val note = actionId.removePrefix("NOTE_CHANGED:")
                setState { copy(note = note) }
            }

            actionId.startsWith("FIELD_CHANGE:note_field:") -> {
                val note = actionId.removePrefix("FIELD_CHANGE:note_field:")
                setState { copy(note = note) }
            }

            actionId.startsWith("BENEFICIARY_SELECTED:") -> {
                val beneficiaryId = actionId.removePrefix("BENEFICIARY_SELECTED:")
                setState { copy(selectedBeneficiaryId = beneficiaryId) }
            }

            actionId == "CONFIRM_PROCEED" -> {
                handleConfirmProceed()
            }

            actionId == "ADD_BENEFICIARY" -> {
                setEffect(
                    TransferEffect.Navigate(NavigationAction(destination = "add_beneficiary")),
                )
            }

            actionId == "VIEW_ALL" -> {
                setEffect(TransferEffect.ShowToast("Not Implemented Yet"))
            }

            actionId == "HEADER_NOTIFICATION" || actionId.startsWith("HEADER_NOTIFICATION") -> {
                setEffect(TransferEffect.ShowToast("Notifications coming soon"))
            }

            else -> {
                val action = state.value.screenModel?.actions?.get(actionId)
                if (action != null) {
                    val dest = action.destination
                    if (dest != null) {
                        setEffect(TransferEffect.Navigate(NavigationAction(destination = dest)))
                    }
                }
            }
        }
    }

    private suspend fun handleConfirmProceed() {
        val currentState = state.value
        val params = SubmitTransferUseCase.Params(
            sourceAccountId = currentState.selectedAccountId,
            beneficiaryId = currentState.selectedBeneficiaryId,
            amount = currentState.amount,
            note = currentState.note.ifBlank { null },
        )
        when (val result = submitTransferUseCase(params)) {
            is Result.Success -> {
                val ref = result.data.referenceNumber.ifBlank { "REF-001" }
                setEffect(TransferEffect.ShowToast("Transfer submitted! Ref: $ref"))
            }
            is Result.Error -> {
                setEffect(TransferEffect.ShowToast(result.message))
            }
            is Result.Loading -> Unit
        }
    }

    private fun ScreenModel.withPatchedBeneficiaryGrid(beneficiaries: List<Beneficiary>): ScreenModel {
        val bensArray = buildJsonArray {
            beneficiaries.forEach { ben ->
                add(buildJsonObject {
                    put("id", ben.id)
                    put("name", ben.name)
                    put("subtitle", ben.subtitle)
                    put("isSelected", ben.isSelected)
                })
            }
        }
        val newProps = buildJsonObject {
            put("beneficiaries", bensArray)
            put("showAddNew", true)
        }
        return copy(
            components = components.map { comp ->
                if (comp.type == SduiComponentType.BENEFICIARY_GRID) comp.copy(props = newProps)
                else comp
            },
        )
    }
}
