package com.ops.opside.flows.sign_on.marketModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_MARKET
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

class MarketInteractor @Inject constructor(
    private val firestore: FirebaseFirestore){

    fun getMarkets(): Observable<MutableList<MarketSE>> {

        return Observable.unsafeCreate { subscriber ->
            try {
                val marketsList = mutableListOf<MarketSE>()

                firestore.collection(DB_TABLE_MARKET)
                    .get()
                    .addOnSuccessListener {

                        for (document in it.documents) {
                            marketsList.add(MarketSE(
                                idFirebase = document.id,
                                name = document.get("name").toString(),
                                address = document.get("address").toString(),
                                latitude = 0.0,
                                longitude = 0.0,
                                document.get("concessionaires").toString().length))
                        }
                        subscriber.onNext(marketsList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun deleteMarket(idFirestore: String): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
        tryOrPrintException {
            firestore.collection(DB_TABLE_MARKET).document(idFirestore).delete()
                .addOnSuccessListener {
                    subscriber.onNext(true)
                    Log.d("FireStoreDelete", "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                    Log.w("FireStoreDelete", "Error deleting document", it)
                }
        }
        }
    }
}