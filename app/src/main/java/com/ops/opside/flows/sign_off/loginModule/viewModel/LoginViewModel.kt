package com.ops.opside.flows.sign_off.loginModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.loginModule.model.LoginInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mLoginInteractor: LoginInteractor
): CommonViewModel() {

    private val _GetUserLogin = MutableLiveData<String>()
    val getUserLogin: LiveData<String> = _GetUserLogin

    fun isSPInitialized(): Boolean{
        return mLoginInteractor.isSPInitialized()
    }

    fun initSP(email: String){
        mLoginInteractor.initSP(email)
    }

    fun getUserLogin(email: String) {
        disposable.add(
            mLoginInteractor.getUserByEmail(email).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _GetUserLogin.value = it
                        showProgress. value = false
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress. value = false
                    }
                )
        )
    }

}