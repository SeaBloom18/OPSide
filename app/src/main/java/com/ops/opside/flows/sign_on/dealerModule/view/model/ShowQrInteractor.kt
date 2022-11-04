package com.ops.opside.flows.sign_on.dealerModule.view.model

import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_ID
import javax.inject.Inject

class ShowQrInteractor @Inject constructor(private val sp: Preferences) {

    fun getFirebaseId() = sp.getString(SP_ID)

}