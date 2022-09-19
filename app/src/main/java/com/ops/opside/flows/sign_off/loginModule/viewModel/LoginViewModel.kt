package com.ops.opside.flows.sign_off.loginModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.loginModule.model.LoginInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mLoginInteractor: LoginInteractor
): CommonViewModel() {

    private val _getUserLogin = MutableLiveData<String>()
    val getUserLogin: LiveData<String> = _getUserLogin

    private val _getUserRole = MutableLiveData<String>()
    val getUserRole: LiveData<String> = _getUserRole

    /*fun isSPInitialized(): Boolean{
        return mLoginInteractor.isSPInitialized()
    }

    fun isRememberMeChecked(): Pair<Boolean, String?>{
        return mLoginInteractor.isRememberMeChecked()
    }*/

    fun initSP(email: String) {
        disposable.add(
            mLoginInteractor.initSP(email).applySchedulers()
                .subscribe(
                    {
                        _getUserRole.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getUserLogin(email: String) {
        disposable.add(
            mLoginInteractor.getUserByEmail(email).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _getUserLogin.value = it
                        showProgress.value = false
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                    }
                )
        )
    }
}