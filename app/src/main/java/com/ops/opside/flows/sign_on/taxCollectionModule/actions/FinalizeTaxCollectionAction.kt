package com.ops.opside.flows.sign_on.taxCollectionModule.actions

sealed class FinalizeTaxCollectionAction {
    object SendCollection: FinalizeTaxCollectionAction()

}