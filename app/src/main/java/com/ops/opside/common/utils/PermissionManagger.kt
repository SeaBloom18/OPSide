package com.ops.opside.common.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PermissionManagger @Inject constructor(
    private val preferences: Preferences
) {
    fun getPermission(): Boolean {
        return preferences.getInt(SP_ROLL) > 3
    }
}