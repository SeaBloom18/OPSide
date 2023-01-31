package com.ops.opside.flows.sign_on.taxCollectionModule.actions

sealed class FinalizeTaxCollectionAction {
    data class ShowMessageError(val error: String): FinalizeTaxCollectionAction()
    object SendCollection: FinalizeTaxCollectionAction()
    object SendEmails: FinalizeTaxCollectionAction()
    object EmailsSent: FinalizeTaxCollectionAction()
    object FinalizeCollection: FinalizeTaxCollectionAction()
}