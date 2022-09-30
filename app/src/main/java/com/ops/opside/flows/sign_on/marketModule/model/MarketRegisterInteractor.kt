package com.ops.opside.flows.sign_on.marketModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_MARKET
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 04/09/22.
 */
class MarketRegisterInteractor @Inject constructor(private val firestore: FirebaseFirestore) {

    fun registerMarket(marketFE: MarketFE): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                firestore.collection(DB_TABLE_MARKET)
                    .add(marketFE.getHashMap())
                    .addOnSuccessListener {
                        Log.d("Firebase", "DocumentSnapshot added with ID: " + it.id)
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                            e -> Log.d("Firebase", "Error adding document", e)
                        subscriber.onNext(false)
                    }
            }
        }
    }

    fun updateMarket(idFirestore: String, name: String, address: String, latitude: Double,
                     longitude: Double): Observable<Boolean>{
        return Observable.unsafeCreate{ subscriber ->
            tryOrPrintException {
                firestore.collection(DB_TABLE_MARKET).document(idFirestore)
                    .update(mapOf(
                        "name" to name,
                        "address" to address,
                        "latitude" to latitude,
                        "longitude" to longitude
                    ))
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                        Log.d("FireStoreUpdateSuccess", "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }
}