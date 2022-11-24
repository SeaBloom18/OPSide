package com.ops.opside.flows.sign_on.dealerModule.view.model

import com.ops.opside.common.utils.*
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 02/11/22.
 */
class DealerInteractor @Inject constructor(private val sp: Preferences) {

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        Triple(sp.getString(SP_NAME), sp.getString(SP_EMAIL), sp.getString(SP_PHONE))

    fun showAboutInfo(): Pair<String?, String?> =
        Pair(sp.getString(SP_ADDRESS), sp.getString(SP_LINE_BUSINESS))

    fun showPricesInfo(): Pair<String?, String?> =
        Pair(sp.getString(SP_ABSENCE), sp.getString(SP_LINEAR_METERS))
}