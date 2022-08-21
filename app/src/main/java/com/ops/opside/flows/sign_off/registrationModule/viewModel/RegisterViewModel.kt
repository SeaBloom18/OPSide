package com.ops.opside.flows.sign_off.registrationModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.registrationModule.model.RegisterInteractor

class RegisterViewModel: CommonViewModel() {

    private val mRegisterInteractor = RegisterInteractor()
    val registerConcessionaire = MutableLiveData<MutableMap<String, Any>>()
    private var dataBaseInstance = FirebaseFirestore.getInstance()

    fun insertConcessionaire(concessionaireSE: ConcessionaireSE){
        disposable.add(
            mRegisterInteractor.registerConcessionaire(concessionaireSE).applySchedulers()
                .subscribe(
                    {
                        dataBaseInstance.collection("concessionaires")
                            .add(concessionaireSE)
                            .addOnSuccessListener { documentReference ->
                                Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.id)
                            }
                            .addOnFailureListener {
                                    e -> Log.w("Firebase", "Error adding document", e)
                            }
                        registerConcessionaire.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
    
}