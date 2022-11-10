package com.ops.opside.flows.sign_on.profileModule.model

import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_NAME
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    val preferences: Preferences
) {
    fun getCollectorName() = preferences.getString(SP_NAME)
    fun deleteProfileData(){
        preferences.deletePreferences()
    }
}