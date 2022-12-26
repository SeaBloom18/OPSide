package com.ops.opside.flows.sign_on.dashboardModule.actions


/**
 * Created by David Alejandro González Quezada on 20/12/22.
 */
sealed class ControlPanelAction {
    object ShowMessageSuccess : ControlPanelAction()
    object ShowMessageError : ControlPanelAction()
}
