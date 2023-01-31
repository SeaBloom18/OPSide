package com.ops.opside.flows.sign_on.incidentsModule.actions

import com.ops.opside.flows.sign_on.marketModule.actions.MarketAction

/**
 * Created by davidgonzalez on 28/01/23
 */
sealed class CreateIncidentAction {
    object ShowMessageSuccess : CreateIncidentAction()
    object ShowMessageError : CreateIncidentAction()
}
