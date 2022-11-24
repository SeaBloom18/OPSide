package com.ops.opside.flows.sign_on.taxCollectionModule.actions

sealed class ConfirmPasswordAction {
    data class ShowMessageError(val error: String) : ConfirmPasswordAction()
    object PasswordSuccess : ConfirmPasswordAction()
}