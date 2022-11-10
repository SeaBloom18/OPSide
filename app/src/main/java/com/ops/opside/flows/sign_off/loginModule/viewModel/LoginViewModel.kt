package com.ops.opside.flows.sign_off.loginModule.viewModel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.BiometricsManager
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.loginModule.actions.LoginAction
import com.ops.opside.flows.sign_off.loginModule.model.LoginInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mLoginInteractor: LoginInteractor
) : CommonViewModel(), BiometricsManager.BiometricListener {

    private val _action: SingleLiveEvent<LoginAction> = SingleLiveEvent()
    fun getAction(): LiveData<LoginAction> = _action

    private var biometricsManager: BiometricsManager? = null

    private val _getUserLogin = MutableLiveData<String>()
    val getUserLogin: LiveData<String> = _getUserLogin

    private val _getUserRole = MutableLiveData<String>()
    val getUserRole: LiveData<String> = _getUserRole

    fun isSPInitialized(): Boolean {
        return mLoginInteractor.isSPInitialized()
    }

    fun isRememberMeChecked(): Triple<Boolean, String?, String?> {
        return mLoginInteractor.isRememberMeChecked()
    }

    fun initSP(email: String, rememberMe: Boolean, useBiometrics: Boolean) {
        disposable.add(
            mLoginInteractor.initSP(email, rememberMe, useBiometrics).applySchedulers()
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

    fun getRol() = mLoginInteractor.getRol()

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

    //fun insertOrigins() = mLoginInteractor.insertOrigins()

    fun checkBiometrics(fragment: Fragment) {
        biometricsManager = BiometricsManager(fragment)
        if (isBiometricsActivated())
            biometricsManager?.loadCredentials(this)
        else
            _action.value = LoginAction.DismissBtnBiometrics
    }

    private fun isBiometricsActivated() = mLoginInteractor.isBiometricsActivated()

    fun saveCredentials(credentials: String) {
        biometricsManager?.saveCredentials(credentials, this)
    }

    override fun onSaveFinished() {
        _action.value = LoginAction.InitSharedPreferences(true)
    }

    override fun onCredentialsError(isSaved: Boolean) {
        if (isSaved) {
            _action.value = LoginAction.LaunchHome
        } else {
            mLoginInteractor.setUseBiometrics(false)
        }
    }

    override fun onKeyErrorDeleted() {
        mLoginInteractor.setUseBiometrics(false)
    }

    override fun onCredentialsLoaded(credential: String) {
        val user = credential.split(":", ignoreCase = false, limit = 2)
            .firstOrNull().orEmpty()

        val pss = credential.split(":", ignoreCase = false, limit = 2)
            .lastOrNull().orEmpty()

        _action.value = LoginAction.TryLogIn(user,pss)
    }
}