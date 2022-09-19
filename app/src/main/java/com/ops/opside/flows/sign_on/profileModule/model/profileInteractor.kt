package com.ops.opside.flows.sign_on.profileModule.model

import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_NAME
import javax.inject.Inject

class profileInteractor @Inject constructor(
    val preferences: Preferences
) {

    fun getCollectorName(): String{
        preferences.getString(SP_NAME)?.let {
            return it
        }.run {
            return "Desconocido"
        }
    }

    fun deleteProfileData(){
        preferences.deletePreferences()
    }

}