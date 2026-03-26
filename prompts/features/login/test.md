# Login — Tests
# cat with: CONTEXT.md + base/testing.md + this file

## Generate these files ONLY
- LoginViewModelTest.kt
- LoginUseCaseTest.kt
- LoginScreenModelParserTest.kt
- LoginScreenTest.kt (Compose UI test)

## LoginViewModelTest — Cover these cases
1. given_sessionExists_whenInit_thenNavigateToDashboardEffect
2. given_noSession_whenInit_thenScreenModelLoaded
3. given_validCredentials_whenSubmit_thenLoadingThenNavigate
4. given_invalidEmail_whenSubmit_thenValidationErrorState
5. given_emptyPassword_whenSubmit_thenValidationErrorState
6. given_apiFailure_whenSubmit_thenErrorStateWithMessage
7. given_forgotAction_whenHandleAction_thenForgotPasswordEffect
8. given_inquireAction_whenHandleAction_thenInquireEffect
9. given_errorState_whenClearError_thenErrorNull
10. given_usernameChanged_whenIntent_thenStateUpdated

## LoginUseCaseTest — Cover these cases
1. given_validParams_whenInvoke_thenRepositoryCalledAndSuccess
2. given_invalidEmailFormat_whenInvoke_thenValidationError
3. given_blankEmail_whenInvoke_thenValidationError
4. given_shortPassword_whenInvoke_thenValidationError
5. given_repositoryFailure_whenInvoke_thenErrorPropagated

## LoginScreenModelParserTest — Cover these cases
1. given_loginScreenJson_whenParsed_thenCorrectScreenId
2. given_loginScreenJson_whenParsed_thenAllComponentsPresent
3. given_loginScreenJson_whenParsed_thenSubmitActionExists
4. given_malformedJson_whenParsed_thenParsingExceptionHandled

## LoginScreenTest (Compose UI) — Cover these cases
1. usernameField_isDisplayed
2. passwordField_isDisplayed
3. loginButton_isDisplayed
4. loginButton_withEmptyFields_showsValidationError
5. biometricRow_isDisplayed
6. forgotLink_isClickable
