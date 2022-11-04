package com.ops.opside.flows.sign_on.dashboardModule.model

import com.ops.opside.common.utils.*
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 02/11/22.
 */

class BottomSheetUserProfileInteractor @Inject constructor(private val sp: Preferences) {

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        Triple(sp.getString(SP_NAME), sp.getString(SP_EMAIL), sp.getString(SP_PHONE))

    fun showAboutInfo(): Pair<String?, String?> =
        Pair(sp.getString(SP_ORIGIN), sp.getString(SP_USER_TYPE))
}