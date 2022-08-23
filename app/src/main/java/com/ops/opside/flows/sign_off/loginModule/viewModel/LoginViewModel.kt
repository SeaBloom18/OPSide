package com.ops.opside.flows.sign_off.loginModule.viewModel

import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.loginModule.model.LoginInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
): CommonViewModel() {

    fun isSPInitialized(): Boolean{
        return loginInteractor.isSPInitialized()
    }

    fun initSP(){
        loginInteractor.initSP()
    }

}