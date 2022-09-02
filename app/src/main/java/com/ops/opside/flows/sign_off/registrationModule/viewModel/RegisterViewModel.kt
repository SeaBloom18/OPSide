package com.ops.opside.flows.sign_off.registrationModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.registrationModule.model.RegisterInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mRegisterInteractor: RegisterInteractor
): CommonViewModel() {

    val registerConcessionaire = MutableLiveData<Boolean>()

    fun insertConcessionaire(concessionaireFE: ConcessionaireFE){
        disposable.add(
            mRegisterInteractor.registerConcessionaire(concessionaireFE).applySchedulers()
                .subscribe(
                    {
                        registerConcessionaire.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun insertForeignConcessionaire(concessionaireFE: ConcessionaireFE){
        disposable.add(
            mRegisterInteractor.registerForeignConcessionaire(concessionaireFE).applySchedulers()
                .subscribe(
                    {
                        registerConcessionaire.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun insertCollector(collectorFE: CollectorFE){
        disposable.add(
            mRegisterInteractor.registerCollector(collectorFE).applySchedulers()
                .subscribe(
                    {
                        registerConcessionaire.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
    
}