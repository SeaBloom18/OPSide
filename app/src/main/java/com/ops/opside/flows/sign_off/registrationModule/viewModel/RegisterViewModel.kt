package com.ops.opside.flows.sign_off.registrationModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.OriginFE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.registrationModule.model.RegisterInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mRegisterInteractor: RegisterInteractor
): CommonViewModel() {

    private val _getOriginList = MutableLiveData<MutableList<OriginFE>>()
    val getOriginList: LiveData<MutableList<OriginFE>> = _getOriginList

    private val _getEmailExists = MutableLiveData<Boolean>()
    val getEmailExists: LiveData<Boolean> = _getEmailExists

    private val registerConcessionaire = MutableLiveData<Boolean>()

    fun insertConcessionaire(concessionaireFE: ConcessionaireFE){
        disposable.add(
            mRegisterInteractor.registerConcessionaire(concessionaireFE).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        registerConcessionaire.value = it
                        showProgress.value = false
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                    }
                )
        )
    }

    fun insertForeignConcessionaire(concessionaireFE: ConcessionaireFE){
        disposable.add(
            mRegisterInteractor.registerForeignConcessionaire(concessionaireFE).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        registerConcessionaire.value = it
                        showProgress.value = false
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                    }
                )
        )
    }

    fun insertCollector(collectorFE: CollectorFE){
        disposable.add(
            mRegisterInteractor.registerCollector(collectorFE).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        registerConcessionaire.value = it
                        showProgress.value = false
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                    }
                )
        )
    }

    fun getOriginList(){
        disposable.add(
            mRegisterInteractor.getOriginList().applySchedulers()
                .subscribe(
                    {
                        _getOriginList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getIsEmailExist(email: String){
        disposable.add(
            mRegisterInteractor.getIsEmailExist(email).applySchedulers()
                .subscribe(
                    {
                        _getEmailExists.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    /*fun getIsExistCollectorEmail(email: String){
        disposable.add(
            mRegisterInteractor.getIsExistCollectorEmail(email).applySchedulers()
                .subscribe(
                    {
                        _getEmailExists.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }*/
}