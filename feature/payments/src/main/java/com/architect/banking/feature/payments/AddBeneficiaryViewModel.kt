package com.architect.banking.feature.payments

import android.content.Context
import com.architect.banking.core.domain.BaseViewModel
import com.architect.banking.core.domain.Result
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.parser.SDUIParser
import com.architect.banking.feature.payments.domain.AddBeneficiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AddBeneficiaryViewModel @Inject constructor(
    private val sduiParser: SDUIParser,
    private val addBeneficiaryUseCase: AddBeneficiaryUseCase,
    @ApplicationContext private val context: Context,
) : BaseViewModel<AddBeneficiaryState, AddBeneficiaryIntent, AddBeneficiaryEffect>() {

    override fun initialState() = AddBeneficiaryState()

    init {
        handleIntent(AddBeneficiaryIntent.LoadScreen)
    }

    override suspend fun reduce(intent: AddBeneficiaryIntent) {
        when (intent) {
            is AddBeneficiaryIntent.LoadScreen -> loadScreen()
            is AddBeneficiaryIntent.HandleAction -> onHandleAction(intent.actionId)
        }
    }

    private suspend fun loadScreen() {
        setState { copy(isLoading = true, error = null) }
        try {
            val json = context.assets
                .open("mock/screens/add_beneficiary_screen.json")
                .bufferedReader()
                .use { it.readText() }
            when (val result = sduiParser.parse(json)) {
                is Result.Success -> setState { copy(isLoading = false, screenModel = result.data) }
                is Result.Error -> setState { copy(isLoading = false, error = result.message) }
                is Result.Loading -> Unit
            }
        } catch (e: Exception) {
            setState { copy(isLoading = false, error = "Failed to load screen: ${e.message}") }
        }
    }

    private suspend fun onHandleAction(actionId: String) {
        when {
            actionId == "NAVIGATE_BACK" -> {
                setEffect(AddBeneficiaryEffect.PopBack)
            }

            actionId.startsWith("FIELD_CHANGE:account_name:") -> {
                val value = actionId.removePrefix("FIELD_CHANGE:account_name:")
                setState { copy(accountName = value) }
            }

            actionId.startsWith("FIELD_CHANGE:bank_name:") -> {
                val value = actionId.removePrefix("FIELD_CHANGE:bank_name:")
                setState { copy(bankName = value) }
            }

            actionId.startsWith("FIELD_CHANGE:account_number:") -> {
                val value = actionId.removePrefix("FIELD_CHANGE:account_number:")
                setState { copy(accountNumber = value) }
            }

            actionId.startsWith("FIELD_CHANGE:nickname:") -> {
                val value = actionId.removePrefix("FIELD_CHANGE:nickname:")
                setState { copy(nickname = value) }
            }

            actionId.startsWith("FIELD_CHANGE:") -> {
                // Handle any other field changes silently
            }

            actionId == "SUBMIT_BENEFICIARY" -> {
                handleSubmitBeneficiary()
            }

            actionId == "HEADER_NOTIFICATION" || actionId.startsWith("HEADER_NOTIFICATION") -> {
                setEffect(AddBeneficiaryEffect.ShowToast("Notifications coming soon"))
            }

            else -> {
                val action = state.value.screenModel?.actions?.get(actionId)
                if (action != null) {
                    val dest = action.destination
                    when {
                        dest == "submit_beneficiary" -> handleSubmitBeneficiary()
                        dest != null -> setEffect(
                            AddBeneficiaryEffect.Navigate(
                                NavigationAction(destination = dest),
                            ),
                        )
                        else -> Unit
                    }
                }
            }
        }
    }

    private suspend fun handleSubmitBeneficiary() {
        val currentState = state.value
        val params = AddBeneficiaryUseCase.Params(
            accountName = currentState.accountName,
            bankName = currentState.bankName,
            accountNumber = currentState.accountNumber,
            nickname = currentState.nickname.ifBlank { null },
        )
        when (val result = addBeneficiaryUseCase(params)) {
            is Result.Success -> {
                setEffect(AddBeneficiaryEffect.ShowToast("Beneficiary saved!"))
                setEffect(AddBeneficiaryEffect.PopBack)
            }
            is Result.Error -> {
                setEffect(AddBeneficiaryEffect.ShowToast(result.message))
            }
            is Result.Loading -> Unit
        }
    }
}
