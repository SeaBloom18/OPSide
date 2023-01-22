package com.ops.opside.common.utils

import android.content.SharedPreferences
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
        priceLinearMeter: Float = 0.0f,
        name: String = "",
        urlPhoto: String = "",
        email: String = "",
        phone: String = "",
        origin: String = "",
        address: String = "",
        lineBusiness: String = "",
        absence: String = "",
        linearMeters: String = "",
        id: String = "",
        roll: Int = 0,
        hasAccess: Boolean = false,
        rememberMe: Boolean = false,
        useBiometrics: Boolean = false
    ){
        sp.edit().putFloat  (SP_PRICE_LINEAR_METER, priceLinearMeter).apply()
        sp.edit().putString (SP_NAME              , name            ).apply()
        sp.edit().putString (SP_USER_URL_PHOTO    , urlPhoto        ).apply()
        sp.edit().putString (SP_EMAIL             , email           ).apply()
        sp.edit().putString (SP_PHONE             , phone           ).apply()
        sp.edit().putString (SP_ORIGIN            , origin          ).apply()
        sp.edit().putString (SP_ADDRESS           , address         ).apply()
        sp.edit().putString (SP_LINE_BUSINESS     , lineBusiness    ).apply()
        sp.edit().putString (SP_ABSENCE           , absence         ).apply()
        sp.edit().putString (SP_LINEAR_METERS     , linearMeters    ).apply()
        sp.edit().putString (SP_USER_TYPE         , phone           ).apply()
        sp.edit().putString (SP_ID                , id              ).apply()
        sp.edit().putInt    (SP_ROLL              , roll            ).apply()
        sp.edit().putBoolean(SP_HAS_ACCESS        , hasAccess       ).apply()
        sp.edit().putBoolean(SP_REMEMBER_ME       , rememberMe      ).apply()
        sp.edit().putBoolean(SP_USE_BIOMETRICS    , useBiometrics   ).apply()
        sp.edit().putBoolean(SP_IS_ON_LINE_MODE   , true            ).apply()
        sp.edit().putBoolean(SP_IS_INITIALIZED    , true            ).apply()
        sp.edit().putString (SP_START_TIME        , ""              ).apply()
        sp.edit().putString (SP_END_TIME          , ""              ).apply()
        sp.edit().putBoolean(SP_IS_INITIALIZED    , true            ).apply()
    }

    fun deletePreferences(){
        sp.edit().remove(SP_PRICE_LINEAR_METER).apply()
        sp.edit().remove(SP_USER_URL_PHOTO    ).apply()
        sp.edit().remove(SP_ID                ).apply()
        sp.edit().remove(SP_PHONE             ).apply()
        sp.edit().remove(SP_ORIGIN            ).apply()
        sp.edit().remove(SP_ADDRESS           ).apply()
        sp.edit().remove(SP_LINE_BUSINESS     ).apply()
        sp.edit().remove(SP_ABSENCE           ).apply()
        sp.edit().remove(SP_LINEAR_METERS     ).apply()
        sp.edit().remove(SP_USER_TYPE         ).apply()
        sp.edit().remove(SP_START_TIME        ).apply()
        sp.edit().remove(SP_END_TIME          ).apply()
        sp.edit().remove(SP_ROLL              ).apply()
        sp.edit().remove(SP_HAS_ACCESS        ).apply()
        sp.edit().remove(SP_IS_ON_LINE_MODE   ).apply()
        sp.edit().remove(SP_IS_INITIALIZED    ).apply()
        sp.edit().remove(SP_USE_BIOMETRICS    ).apply()
    }
}

const val SP_PRICE_LINEAR_METER = "priceLinealMeter"
const val SP_NAME               = "name"
const val SP_USER_URL_PHOTO     = "urlPhoto"
const val SP_EMAIL              = "email"
const val SP_PHONE              = "phone"
const val SP_ORIGIN             = "origin"
const val SP_ADDRESS            = "address"
const val SP_LINE_BUSINESS      = "lineBusiness"
const val SP_ABSENCE            = "absence"
const val SP_LINEAR_METERS      = "linearMeters"
const val SP_USER_TYPE          = "userType"
const val SP_START_TIME         = "startTime"
const val SP_END_TIME           = "endTime"
const val SP_ID                 = "id"
const val SP_ROLL               = "roll"
const val SP_HAS_ACCESS         = "hasAccess"
const val SP_REMEMBER_ME        = "rememberMe"
const val SP_IS_ON_LINE_MODE    = "isOnLineMode"
const val SP_USE_BIOMETRICS     = "useBiometrics"
const val SP_IS_INITIALIZED     = "isInitialized"
