package com.ops.opside.common.bsd.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.DB_TABLE_MARKET
import io.reactivex.Observable
import javax.inject.Inject

class BottomSheetFilterInteractor @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    fun getCollectors(): Observable<MutableMap<String,String>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_COLLECTOR)
                    .get()
                    .addOnSuccessListener {
                        val collectors = mutableMapOf<String,String>()
                        for (document in it.documents) {
                            collectors[(document.get("name").toString())] = document.id
                        }
                        subscriber.onNext(collectors)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun getMarkets(): Observable<MutableMap<String,String>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_MARKET)
                    .whereEqualTo("isDeleted",false)
                    .get()
                    .addOnSuccessListener {
                        val markets = mutableMapOf<String,String>()
                        for (document in it.documents) {
                            markets[(document.get("name").toString())] = document.id
                        }
                        subscriber.onNext(markets)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

}