package com.ops.opside.flows.sign_on.taxCollectionModule.actions

import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

sealed class TaxCollectionAction {

    data class SendReceipt(val email: TaxCollectionActivity.Email) : TaxCollectionAction()
    data class ShowMessageError(val error: String) : TaxCollectionAction()
    data class SetCollectorName(val collectorName: String) : TaxCollectionAction()
    object ResetActivity: TaxCollectionAction()

}