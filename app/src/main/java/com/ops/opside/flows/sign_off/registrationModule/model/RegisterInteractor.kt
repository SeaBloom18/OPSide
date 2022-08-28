package com.ops.opside.flows.sign_off.registrationModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.Constants
import io.reactivex.Observable
import javax.inject.Inject

class RegisterInteractor @Inject constructor(
    private val firestore: FirebaseFirestore){

    fun registerConcessionaire(concessionaireFE: ConcessionaireFE): Observable<Boolean>{
        return Observable.unsafeCreate{ subscriber ->
            try {
                firestore.collection(Constants.FIRESTORE_CONCESSIONAIRES)
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
                firestore.collection(Constants.FIRESTORE_CONCESSIONAIRES)
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
                    firestore.collection(Constants.FIRESTORE_COLLECTOR)
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