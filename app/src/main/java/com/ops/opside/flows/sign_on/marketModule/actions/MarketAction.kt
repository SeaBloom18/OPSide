package com.ops.opside.flows.sign_on.marketModule.actions

import com.ops.opside.flows.sign_on.dashboardModule.actions.ControlPanelAction

/**
 * Created by David Alejandro González Quezada on 20/12/22.
 */
sealed class MarketAction {
    object ShowMessageSuccess : MarketAction()
    object ShowMessageError : MarketAction()
}
