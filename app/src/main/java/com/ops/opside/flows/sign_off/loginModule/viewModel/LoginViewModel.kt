package com.ops.opside.flows.sign_off.loginModule.viewModel

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
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
    private val mLoginInteractor: LoginInteractor,
    private val application: Application
) : CommonViewModel(), BiometricsManager.BiometricListener {

    private val _action: SingleLiveEvent<LoginAction> = SingleLiveEvent()
    fun getAction(): LiveData<LoginAction> = _action

    private val _getLinealMetersPrice = MutableLiveData<Double>()
    val getLinealMetersPrice: LiveData<Double> = _getLinealMetersPrice

    private var biometricsManager: BiometricsManager? = null

    fun isSPInitialized() = mLoginInteractor.isSPInitialized()
    fun isRememberMe() = mLoginInteractor.isRememberMe()
    fun updateRememberMe(rememberMe: Boolean) = mLoginInteractor.updateRememberMe(rememberMe)
    fun getLoginSp() = mLoginInteractor.getLoginSp()

    fun initSPForCollector(
        collector: CollectorFE,
        linearMeterPrice: Double,
        rememberMe: Boolean,
        useBiometrics: Boolean
    ): Pair<Boolean, String> {
        return mLoginInteractor.initSPForCollector(
            collector, linearMeterPrice, rememberMe,
            useBiometrics
        )
    }

    fun initSPForConcessionaire(
        concessionaire: ConcessionaireFE,
        rememberMe: Boolean,
        useBiometrics: Boolean
    ): Pair<Boolean, String> {
        return mLoginInteractor.initSPForConcessionaire(concessionaire, rememberMe, useBiometrics)
    }

    fun getLinealMeterPrice() {
        disposable.add(
            mLoginInteractor.getLinearMetersPrice().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getLinealMetersPrice.value = it
                    },
                    {
                        showProgress.value = false
                        LoginAction.ShowMessageError(it.message.toString())
                    }
                )
        )
    }

    fun searchForCollector(email: String) {
        disposable.add(
            mLoginInteractor.getCollectorByEmail(email).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        if (it.idFirebase.isNotEmpty()) {
                            _action.value = LoginAction.SetUserData(collector = it)
                        } else {
                            searchForConcessionaire(email)
                        }
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                        _action.value =
                            LoginAction.ShowMessageError(it.message.toString())
                    }
                )
        )
    }

    private fun searchForConcessionaire(email: String) {
        disposable.add(
            mLoginInteractor.getConcessionaireByEmail(email).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        if (it.idFirebase.isNotEmpty()) {
                            _action.value = LoginAction.SetUserData(concessionaire = it)
                        } else {
                            showProgress.value = false
                            _action.value =
                                LoginAction.ShowMessageError(
                                    application.getString(R.string.login_toast_credentials_validation)
                                )
                        }
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                        _action.value =
                            LoginAction.ShowMessageError(it.message.toString())
                    }
                )
        )
    }

    fun getCurrentVersion() {
        disposable.add(
            mLoginInteractor.getCurrentVersion().applySchedulers()
                .subscribe(
                    {
                        if (it) {
                            _action.value = LoginAction.LaunchHome
                        } else {
                            _action.value =
                                LoginAction.ShowMessageError(application.getString(R.string.login_new_version))
                        }
                    },
                    {
                        Log.e("Error", it.toString())
                        _action.value =
                            LoginAction.ShowMessageError(it.message.toString())
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

    fun isBiometricsActivated() = mLoginInteractor.isBiometricsActivated()

    fun saveCredentials(credentials: String) {
        biometricsManager?.saveCredentials(credentials, this)
    }

    override fun onSaveFinished() {
        _action.value = LoginAction.InitSharedPreferences(true)
    }

    override fun onCredentialsError(isSaved: Boolean) {
        if (isSaved.not()) {
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

        _action.value = LoginAction.TryLogIn(user, pss)
    }
}