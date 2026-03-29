package com.architect.banking.feature.payments

import com.architect.banking.core.domain.UiEffect
import com.architect.banking.core.domain.UiIntent
import com.architect.banking.core.domain.UiState
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.model.ScreenModel

// ──────────────── Transfer Screen ────────────────

data class TransferState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedAccountId: String = "acc_003",
    val selectedBeneficiaryId: String = "",
    val amount: Double = 0.0,
    val note: String = "",
) : UiState

sealed class TransferIntent : UiIntent {
    object LoadScreen : TransferIntent()
    data class HandleAction(val actionId: String) : TransferIntent()
}

sealed class TransferEffect : UiEffect {
    data class Navigate(val action: NavigationAction) : TransferEffect()
    data class ShowToast(val message: String) : TransferEffect()
    data class ShowDialog(val message: String) : TransferEffect()
}

// ──────────────── Add Beneficiary Screen ────────────────

data class AddBeneficiaryState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val accountName: String = "",
    val bankName: String = "",
    val accountNumber: String = "",
    val nickname: String = "",
) : UiState

sealed class AddBeneficiaryIntent : UiIntent {
    object LoadScreen : AddBeneficiaryIntent()
    data class HandleAction(val actionId: String) : AddBeneficiaryIntent()
}

sealed class AddBeneficiaryEffect : UiEffect {
    data class Navigate(val action: NavigationAction) : AddBeneficiaryEffect()
    data class ShowToast(val message: String) : AddBeneficiaryEffect()
    object PopBack : AddBeneficiaryEffect()
}
