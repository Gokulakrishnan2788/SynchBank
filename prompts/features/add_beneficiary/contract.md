# Add Beneficiary — MVI Contract
# cat with: CONTEXT.md + contract/mvi.md + this file

## AddBeneficiaryState
data class AddBeneficiaryState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,
    val bankInstitutions: List<BankInstitution> = emptyList(),
    // Form fields
    val accountName: String = "",
    val selectedBank: String = "",
    val accountNumber: String = "",
    val nickname: String = "",
    // Validation errors per field
    val accountNameError: String? = null,
    val bankNameError: String? = null,
    val accountNumberError: String? = null,
    // Result
    val result: AddBeneficiaryResult? = null
) : UiState

## AddBeneficiaryIntent
sealed class AddBeneficiaryIntent : UiIntent {
    object LoadScreen : AddBeneficiaryIntent()
    data class AccountNameChanged(val value: String) : AddBeneficiaryIntent()
    data class BankSelected(val bankName: String) : AddBeneficiaryIntent()
    data class AccountNumberChanged(val value: String) : AddBeneficiaryIntent()
    data class NicknameChanged(val value: String) : AddBeneficiaryIntent()
    object Submit : AddBeneficiaryIntent()
    object NavigateBack : AddBeneficiaryIntent()
    data class HandleAction(val actionId: String) : AddBeneficiaryIntent()
    object ClearError : AddBeneficiaryIntent()
}

## AddBeneficiaryEffect
sealed class AddBeneficiaryEffect : UiEffect {
    object NavigateBack : AddBeneficiaryEffect()
    data class ShowSuccess(val message: String) : AddBeneficiaryEffect()
    data class ShowToast(val message: String) : AddBeneficiaryEffect()
    data class ShowError(val message: String) : AddBeneficiaryEffect()
}

## Reducer Logic
LoadScreen              → GetBankInstitutionsUseCase → setState(bankInstitutions, screenModel)
                          isLoading=true during load → ScreenLoadingBox shown
AccountNameChanged      → setState(accountName, clear accountNameError)
BankSelected            → setState(selectedBank, clear bankNameError)
AccountNumberChanged    → setState(accountNumber, clear accountNumberError)
NicknameChanged         → setState(nickname)
Submit                  → isSubmitting=true
                          → AddBeneficiaryUseCase(accountName, selectedBank, accountNumber, nickname)
                          → Success: setEffect(ShowSuccess("Beneficiary added successfully")) + setEffect(NavigateBack)
                          → ValidationError: setState(fieldErrors per field)
                          → ApiError: setState(error) + setEffect(ShowError)
HandleAction("NAVIGATE_BACK") → setEffect(NavigateBack)
HandleAction("SUBMIT_BENEFICIARY") → same as Submit
NavigateBack            → setEffect(NavigateBack)
ClearError              → setState(error=null, all fieldErrors=null)

## Navigation
- AddBeneficiaryScreen lives at route "add_beneficiary"
- Navigated to from TransferScreen when "NEW RECIPIENT" tile tapped
- NavigateBack effect → navController.popBackStack() → back to TransferScreen
- Must NOT navigate to any tab or other screen except back to payments
