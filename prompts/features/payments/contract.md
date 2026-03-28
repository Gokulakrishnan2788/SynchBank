# Payments (Transfer) — MVI Contract

## TransferState
data class TransferState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,
    val sourceAccounts: List<SourceAccount> = emptyList(),
    val selectedAccount: SourceAccount? = null,
    val beneficiaryGrid: BeneficiaryGrid? = null,
    val selectedBeneficiary: Beneficiary? = null,
    val amount: Double = 0.0,
    val amountDisplay: String = "0.00",
    val note: String = "",
    val transferResult: TransferResult? = null
) : UiState

## TransferIntent
sealed class TransferIntent : UiIntent {
    object LoadScreen : TransferIntent()
    data class SelectSourceAccount(val account: SourceAccount) : TransferIntent()
    data class AmountChanged(val value: Double) : TransferIntent()
    data class QuickAmountTapped(val value: Double) : TransferIntent()
    data class NoteChanged(val value: String) : TransferIntent()
    data class SelectBeneficiary(val beneficiary: Beneficiary) : TransferIntent()
    object AddNewBeneficiary : TransferIntent()
    object ConfirmAndProceed : TransferIntent()
    data class HandleAction(val actionId: String) : TransferIntent()
    object ClearError : TransferIntent()
    object ResetForm : TransferIntent()
}

## TransferEffect
sealed class TransferEffect : UiEffect {
    object NavigateToAddBeneficiary : TransferEffect()
    data class ShowSuccess(val referenceNumber: String) : TransferEffect()
    data class ShowToast(val message: String) : TransferEffect()
    data class ShowError(val message: String) : TransferEffect()
}

## Reducer Logic
LoadScreen          → GetSourceAccountsUseCase + GetBeneficiariesUseCase in parallel
                      → setState(sourceAccounts, selectedAccount=first, beneficiaryGrid, screenModel)
SelectSourceAccount → setState(selectedAccount)
AmountChanged       → setState(amount, amountDisplay=formatted)
QuickAmountTapped   → setState(amount += value, amountDisplay updated)
NoteChanged         → setState(note)
SelectBeneficiary   → setState(selectedBeneficiary)
AddNewBeneficiary   → setEffect(NavigateToAddBeneficiary)
ConfirmAndProceed   → isSubmitting=true → SubmitTransferUseCase →
                       success: setState(transferResult) + setEffect(ShowSuccess(ref))
                       fail:    setState(error) + setEffect(ShowError)
HandleAction("ADD_BENEFICIARY") → setEffect(NavigateToAddBeneficiary)
HandleAction("VIEW_ALL")        → setEffect(ShowToast("Not implemented yet"))
ClearError          → setState(error=null)
ResetForm           → setState keeping sourceAccounts+beneficiaryGrid, reset amount/note/selected

## AddBeneficiaryState
data class AddBeneficiaryState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,
    val accountName: String = "",
    val bankName: String = "",
    val accountNumber: String = "",
    val nickname: String = "",
    val result: AddBeneficiaryResult? = null
) : UiState

## AddBeneficiaryIntent
sealed class AddBeneficiaryIntent : UiIntent {
    object LoadScreen : AddBeneficiaryIntent()
    data class AccountNameChanged(val value: String) : AddBeneficiaryIntent()
    data class BankNameChanged(val value: String) : AddBeneficiaryIntent()
    data class AccountNumberChanged(val value: String) : AddBeneficiaryIntent()
    data class NicknameChanged(val value: String) : AddBeneficiaryIntent()
    object Submit : AddBeneficiaryIntent()
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

## AddBeneficiary Reducer
LoadScreen → load add_beneficiary_screen.json → setState(screenModel)
Submit     → AddBeneficiaryUseCase → success: ShowSuccess + NavigateBack / fail: ShowError
Back press → NavigateBack effect
