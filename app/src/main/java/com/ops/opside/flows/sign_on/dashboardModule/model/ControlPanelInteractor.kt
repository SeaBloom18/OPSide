package com.ops.opside.flows.sign_on.dashboardModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.DB_TABLE_MARKET
import com.ops.opside.common.entities.DB_TABLE_RESOURCES
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 10/11/22.
 */
data class ControlPanelInteractor @Inject constructor(private val firestore: FirebaseFirestore) {

    fun getCollectors(): Observable<MutableList<CollectorSE>> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                val collectorList = mutableListOf<CollectorSE>()

                firestore.collection(DB_TABLE_COLLECTOR)
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            collectorList.add(CollectorSE(
                                idFirebase = document.id,
                                name = document.get("name").toString(),
                                address = document.get("address").toString(),
                                hasAccess = document.get("hasAccess") as Boolean,
                                email = document.get("email").toString(),
                                phone = document.get("phone").toString()))
                        }
                        subscriber.onNext(collectorList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }

    fun getLinealPriceMeter(): Observable<String> {
        return Observable.unsafeCreate{ subscriber ->
            var priceLinearMeter = ""
            firestore.collection(DB_TABLE_RESOURCES)
                .get()
                .addOnSuccessListener {
                    for (document in it.documents) {
                        priceLinearMeter = document.get("priceLinealMeter").toString()
                    }
                    subscriber.onNext(priceLinearMeter)
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

    fun updateLinealPriceMeter(idFirestore: String, price: String): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                firestore.collection(DB_TABLE_RESOURCES).document(idFirestore).update("priceLinealMeter", price)
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                        Log.d("FireStoreDelete", "DocumentSnapshot successfully updated!")
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                        Log.w("FireStoreDelete", "Error deleting updated", it)
                    }
            }
        }
    }
}