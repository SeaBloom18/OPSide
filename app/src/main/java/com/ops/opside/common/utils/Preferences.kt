package com.ops.opside.common.utils

import android.content.SharedPreferences
import io.reactivex.Observable
import javax.inject.Inject

class Preferences @Inject constructor(
    private val sp: SharedPreferences
) {
    
    fun putValue(key: String, data: String) = sp.edit().putString(key, data).apply()
    fun putValue(key: String, data: Boolean) = sp.edit().putBoolean(key, data).apply()
    fun putValue(key: String, data: Float) = sp.edit().putFloat(key, data).apply()
    fun putValue(key: String, data: Int) = sp.edit().putInt(key, data).apply()

    fun deleteValue(key: String) = sp.edit().remove(key).apply()

    fun getString(key: String) = sp.getString(key, "")
    fun getBoolean(key: String) = sp.getBoolean(key, false)
    fun getFloat(key: String) = sp.getFloat(key, -1f)
    fun getInt(key: String) = sp.getInt(key, -1)

    fun initPreferences(
        priceLinearMeter: Float,
        name: String,
        email: String,
        id: String,
        roll: Int,
        hasAccess: Boolean,
        rememberMe: Boolean
    ){
        sp.edit().putFloat  (SP_PRICE_LINEAR_METER, priceLinearMeter).apply()
        sp.edit().putString (SP_NAME              , name            ).apply()
        sp.edit().putString (SP_EMAIL             , email           ).apply()
        sp.edit().putString (SP_ID                , id              ).apply()
        sp.edit().putInt    (SP_ROLL              , roll            ).apply()
        sp.edit().putBoolean(SP_HAS_ACCESS        , hasAccess       ).apply()
        sp.edit().putBoolean(SP_REMEMBER_ME       , rememberMe      ).apply()
        sp.edit().putBoolean(SP_IS_ON_LINE_MODE   , true            ).apply()
        sp.edit().putBoolean(SP_IS_INITIALIZED    , true            ).apply()
        sp.edit().putString (SP_START_TIME        , ""              ).apply()
        sp.edit().putString (SP_END_TIME          , ""              ).apply()
        sp.edit().putBoolean(SP_IS_INITIALIZED    , true            ).apply()
    }

    fun deletePreferences(){
        sp.edit().remove(SP_PRICE_LINEAR_METER).apply()
        sp.edit().remove(SP_NAME              ).apply()
        sp.edit().remove(SP_ID                ).apply()
        sp.edit().remove(SP_EMAIL             ).apply()
        sp.edit().remove(SP_START_TIME        ).apply()
        sp.edit().remove(SP_END_TIME          ).apply()
        sp.edit().remove(SP_ROLL              ).apply()
        sp.edit().remove(SP_HAS_ACCESS        ).apply()
        sp.edit().remove(SP_IS_ON_LINE_MODE   ).apply()
    }

}

const val SP_PRICE_LINEAR_METER = "priceLinearMeter"
const val SP_NAME               = "name"
const val SP_EMAIL              = "email"
const val SP_START_TIME         = "startTime"
const val SP_END_TIME           = "endTime"
const val SP_ID                 = "id"
const val SP_ROLL               = "roll"
const val SP_HAS_ACCESS         = "hasAccess"
const val SP_REMEMBER_ME        = "rememberMe"
const val SP_IS_ON_LINE_MODE    = "isOnLineMode"
const val SP_IS_INITIALIZED     = "isInitialized"
