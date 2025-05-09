package com.ops.opside.flows.sign_off.registrationModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.DB_TABLE_ORIGIN
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.OriginFE
import com.ops.opside.common.utils.getName
import io.reactivex.Observable
import javax.inject.Inject

class RegisterInteractor @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun registerConcessionaire(concessionaireFE: ConcessionaireFE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .add(concessionaireFE.getHashMap())
                    .addOnSuccessListener { _ ->
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener { e ->
                        Log.d("Firebase", "Error adding document", e)
                        subscriber.onNext(false)
                    }

            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun registerForeignConcessionaire(concessionaireFE: ConcessionaireFE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .add(concessionaireFE.getHashMap())
                    .addOnSuccessListener { documentReference ->
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firebase", "Error adding document", e)
                        subscriber.onNext(false)
                    }

            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun registerCollector(collectorFE: CollectorFE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Collector.getName())
                    .add(collectorFE.getHashMap())
                    .addOnSuccessListener { documentReference ->
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firebase", "Error adding document", e)
                        subscriber.onNext(false)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }


    fun getOriginList(): Observable<MutableList<OriginFE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val originList: MutableList<OriginFE> = mutableListOf()
                firestore.collection(DB_TABLE_ORIGIN)
                    .get()
                    .addOnSuccessListener {

                        for (document in it.documents) {
                            originList.add(
                                OriginFE(
                                    document.id,
                                    document.get("originName").toString()
                                )
                            )
                        }
                        val sortedByName = originList.sortedBy { myObject -> myObject.originName }
                        subscriber.onNext(sortedByName as MutableList<OriginFE>?)
                    }.addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun getIsEmailExist(email: String): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener {
                        if (it.documents.size > 0) {
                            subscriber.onNext(true)
                        } else {
                            firestore.collection(TablesEnum.Collector.getName())
                                .whereEqualTo("email", email)
                                .get()
                                .addOnSuccessListener {
                                    if (it.documents.size > 0) {
                                        subscriber.onNext(true)
                                    } else {
                                        subscriber.onNext(false)
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("loginFirestore", "Error getting documents: ", exception)
                                    subscriber.onError(exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("loginFirestore", "Error getting documents: ", exception)
                        subscriber.onError(exception)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }
}


