package com.ops.opside.flows.sign_on.dashboardModule.interfaces

/**
 * Created by David Alejandro González Quezada on 20/12/22.
 */
interface ControlPanelInterface {
    fun switchHasAccess(idFirestore: String, hasAccess: Boolean)
}