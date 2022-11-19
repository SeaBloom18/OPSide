package com.ops.opside.flows.sign_off.registrationModule.actions

sealed class RegistrationAction {

    object ShowMessageSuccess : RegistrationAction()
    object ShowMessageError : RegistrationAction()

}
