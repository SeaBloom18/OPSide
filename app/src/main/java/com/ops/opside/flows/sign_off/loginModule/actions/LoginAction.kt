package com.ops.opside.flows.sign_off.loginModule.actions

import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE

sealed class LoginAction {
    data class InitSharedPreferences(val useBiometrics: Boolean) : LoginAction()
    data class TryLogIn(val user: String, val pass: String) : LoginAction()
    data class ShowMessageError(val error: String) : LoginAction()

    data class SetUserData(
        val collector: CollectorFE? = null,
        val concessionaire: ConcessionaireFE? = null
    ) : LoginAction()

    object DismissBtnBiometrics : LoginAction()
    object LaunchHome : LoginAction()
}