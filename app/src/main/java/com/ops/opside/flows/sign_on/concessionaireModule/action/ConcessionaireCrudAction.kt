package com.ops.opside.flows.sign_on.concessionaireModule.action

import com.ops.opside.common.entities.share.ParticipatingConcessSE

sealed class ConcessionaireCrudAction {
    object SuccesRelation : ConcessionaireCrudAction()
    data class SucessEliminationRelation(val relation: ParticipatingConcessSE) :
        ConcessionaireCrudAction()

    data class MessageError(val error: String) : ConcessionaireCrudAction()
}