package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.ops.opside.common.utils.BiometricsManager
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.loginModule.actions.LoginAction
import com.ops.opside.flows.sign_off.loginModule.model.LoginInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.FinalizeTaxCollectionAction
import com.ops.opside.flows.sign_on.taxCollectionModule.model.FinalizeTaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinalizeTaxCollectionViewModel @Inject constructor(
    private val mFinalizeTaxCollectionInteractor: FinalizeTaxCollectionInteractor,
    private val mLoginInteractor: LoginInteractor
) : CommonViewModel(), BiometricsManager.BiometricListener {

    private val _action: SingleLiveEvent<FinalizeTaxCollectionAction> = SingleLiveEvent()
    fun getAction(): LiveData<FinalizeTaxCollectionAction> = _action

    private var biometricsManager: BiometricsManager? = null

    fun checkBiometrics(fragment: Fragment) {
        biometricsManager = BiometricsManager(fragment)
        if (isBiometricsActivated())
            biometricsManager?.loadCredentials(this)
        //else
        //_action.value = Promp Contraseña
    }

    private fun isBiometricsActivated() = mLoginInteractor.isBiometricsActivated()

    override fun onSaveFinished() {
        //doNothing
    }

    override fun onCredentialsError(isSaved: Boolean) {
        if (isSaved) {
            //_action.value = Promp Contraseña
        } else {
            mLoginInteractor.setUseBiometrics(false)
        }
    }

    override fun onKeyErrorDeleted() {
        mLoginInteractor.setUseBiometrics(false)
    }

    override fun onCredentialsLoaded(credential: String) {
        _action.value = FinalizeTaxCollectionAction.SendCollection
    }

}