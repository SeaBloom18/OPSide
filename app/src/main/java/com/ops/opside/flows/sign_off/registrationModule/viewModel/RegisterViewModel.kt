package com.ops.opside.flows.sign_off.registrationModule.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.registrationModule.model.RegisterInteractor

class RegisterViewModel: CommonViewModel() {

    private val mRegisterInteractor = RegisterInteractor()
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