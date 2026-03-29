package com.architect.banking.feature.accounts

import com.architect.banking.core.domain.UiEffect
import com.architect.banking.core.domain.UiIntent
import com.architect.banking.core.domain.UiState
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.model.ScreenModel

data class AccountsState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

sealed class AccountsIntent : UiIntent {
    object LoadScreen : AccountsIntent()
    data class HandleAction(val actionId: String) : AccountsIntent()
}

sealed class AccountsEffect : UiEffect {
    data class Navigate(val action: NavigationAction) : AccountsEffect()
    data class ShowToast(val message: String) : AccountsEffect()
}
