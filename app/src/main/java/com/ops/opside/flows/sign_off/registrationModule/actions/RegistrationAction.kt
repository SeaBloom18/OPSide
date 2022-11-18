package com.ops.opside.flows.sign_off.registrationModule.actions

import com.ops.opside.common.entities.firestore.ConcessionaireFE

sealed class RegistrationAction {

    data class InsertConcessionaire(val concessionaireFE: ConcessionaireFE) : RegistrationAction()
    data class ShowMessageError(val error: String) : RegistrationAction()

}
