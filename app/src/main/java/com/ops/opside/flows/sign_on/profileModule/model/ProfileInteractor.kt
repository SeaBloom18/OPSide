package com.ops.opside.flows.sign_on.profileModule.model

import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_ID
import com.ops.opside.common.utils.SP_NAME
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    val preferences: Preferences
) {
    fun getCollectorName() = preferences.getString(SP_NAME)
    fun getCollectorId() = preferences.getString(SP_ID)
    fun deleteProfileData(){
        preferences.deletePreferences()
    }
}