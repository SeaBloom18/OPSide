package com.ops.opside.flows.sign_off.loginModule.viewModel

import androidx.lifecycle.ViewModel
import com.ops.opside.flows.sign_off.loginModule.model.LoginInteractor

class LoginViewModel: ViewModel() {

    val loginInteractor: LoginInteractor

    init {
        loginInteractor = LoginInteractor("","")
    }

    fun getUser(){

    }
}