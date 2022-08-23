package com.ops.opside.flows.sign_off.registrationModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import io.reactivex.Observable

class RegisterInteractor {

    private var dataBaseInstance = FirebaseFirestore.getInstance()

    fun registerConcessionaire(concessionaireFE: ConcessionaireFE): Observable<Boolean>{
        return Observable.unsafeCreate{ subscriber ->
            try {

                dataBaseInstance.collection("concessionaires")
                    .add(concessionaireFE.getHashMap())
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.id)
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                            e -> Log.d("Firebase", "Error adding document", e)
                        subscriber.onNext(false)
                    }

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun registerForeignConcessionaire(concessionaireFE: ConcessionaireFE): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
            try {
                dataBaseInstance.collection("concessionaires")
                    .add(concessionaireFE.getHashMap())
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.id)
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                            e -> Log.w("Firebase", "Error adding document", e)
                        subscriber.onNext(false)
                    }

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun registerCollector(collectorFE: CollectorFE): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
            try {
                dataBaseInstance.collection("collectors")
                    .add(collectorFE)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.id)
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                            e -> Log.w("Firebase", "Error adding document", e)
                        subscriber.onNext(false)

                    }

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}