package com.ops.opside.flows.sign_off.loginModule.actions

sealed class LoginAction {
    data class InitSharedPreferences(val useBiometrics: Boolean): LoginAction()
    data class TryLogIn(val user: String, val pass: String): LoginAction()
    object DismissBtnBiometrics: LoginAction()
    object LaunchHome: LoginAction()
}