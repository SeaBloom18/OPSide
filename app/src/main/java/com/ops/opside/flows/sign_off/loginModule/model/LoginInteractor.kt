package com.ops.opside.flows.sign_off.loginModule.model

import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_IS_INITIALIZED
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val sp: Preferences
) {

    fun isSPInitialized(): Boolean{
        return sp.getBoolean(SP_IS_INITIALIZED).not()
    }

    fun initSP(){
        sp.initPreferences(
            15.5f,
            "Mario Armando Razo Valenzuela",
            "l8oik7bgrvfde",
            3,
            true,
            true
        )
    }

}