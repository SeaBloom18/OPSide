package com.ops.opside.common.utils

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private fun getSP(context: Context): SharedPreferences = context.getSharedPreferences("main", Context.MODE_PRIVATE)

    fun putValue(context: Context, key: String, data: String) = getSP(context).edit().putString(key, data).apply()
    fun putValue(context: Context, key: String, data: Boolean) = getSP(context).edit().putBoolean(key, data).apply()
    fun putValue(context: Context, key: String, data: Float) = getSP(context).edit().putFloat(key, data).apply()
    fun putValue(context: Context, key: String, data: Int) = getSP(context).edit().putInt(key, data).apply()

    fun deleteValue(context: Context, key: String) = getSP(context).edit().remove(key).apply()

    fun getString(context: Context, key: String) = getSP(context).getString(key, "")
    fun getBoolean(context: Context, key: String) = getSP(context).getBoolean(key, false)
    fun getFloat(context: Context, key: String) = getSP(context).getFloat(key, -1f)
    fun getInt(context: Context, key: String) = getSP(context).getInt(key, -1)

    fun initPreferences(
        context: Context,
        priceLinearMeter: Float,
        name: String,
        id: String,
        roll: Int,
        hasAccess: Boolean,
        isOnLineMode: Boolean
    ){
        val sp = getSP(context)
        sp.edit().putFloat  (SP_PRICE_LINEAR_METER, priceLinearMeter).apply()
        sp.edit().putString (SP_NAME              , name            ).apply()
        sp.edit().putString (SP_ID                , id              ).apply()
        sp.edit().putInt    (SP_ROLL              , roll            ).apply()
        sp.edit().putBoolean(SP_HAS_ACCESS        , hasAccess       ).apply()
        sp.edit().putBoolean(SP_IS_ON_LINE_MODE   , isOnLineMode    ).apply()
        sp.edit().putBoolean(SP_IS_INITIALIZED    , true            ).apply()
    }

    fun deletePreferences(context: Context){
        val sp = getSP(context)
        sp.edit().remove(SP_PRICE_LINEAR_METER).apply()
        sp.edit().remove(SP_NAME              ).apply()
        sp.edit().remove(SP_ID                ).apply()
        sp.edit().remove(SP_ROLL              ).apply()
        sp.edit().remove(SP_HAS_ACCESS        ).apply()
        sp.edit().remove(SP_IS_ON_LINE_MODE   ).apply()
    }

    const val SP_PRICE_LINEAR_METER = "priceLinearMeter"
    const val SP_NAME               = "name"
    const val SP_ID                 = "id"
    const val SP_ROLL               = "roll"
    const val SP_HAS_ACCESS         = "hasAccess"
    const val SP_IS_ON_LINE_MODE    = "isOnLineMode"
    const val SP_IS_INITIALIZED     = "isInitialized"

}
