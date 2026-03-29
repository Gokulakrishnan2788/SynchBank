package com.architect.banking.feature.accounts

import android.content.Context
import com.architect.banking.core.domain.BaseViewModel
import com.architect.banking.core.domain.Result
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.parser.SDUIParser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val sduiParser: SDUIParser,
    @ApplicationContext private val context: Context,
) : BaseViewModel<AccountsState, AccountsIntent, AccountsEffect>() {

    override fun initialState() = AccountsState()

    init {
        handleIntent(AccountsIntent.LoadScreen)
    }

    override suspend fun reduce(intent: AccountsIntent) {
        when (intent) {
            is AccountsIntent.LoadScreen -> loadScreen()
            is AccountsIntent.HandleAction -> onHandleAction(intent.actionId)
        }
    }

    private suspend fun loadScreen() {
        setState { copy(isLoading = true, error = null) }
        try {
            val json = context.assets
                .open("mock/screens/accounts_screen.json")
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

    private fun onHandleAction(actionId: String) {
        when {
            actionId == "HEADER_NOTIFICATION" -> {
                setEffect(AccountsEffect.ShowToast("Notifications coming soon"))
            }
            else -> {
                setEffect(AccountsEffect.ShowToast("Not Implemented Yet"))
            }
        }
    }
}
