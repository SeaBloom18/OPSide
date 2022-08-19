package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.firestore.MarketFE
import io.reactivex.Observable


class PickMarketInteractor {
    private val firestore = FirebaseFirestore.getInstance()

    fun getMarketsList(): Observable<MutableList<MarketFE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val markets: MutableList<MarketFE> = mutableListOf()

                firestore.collection("markets")
                    .get()
                    .addOnSuccessListener {

                        for (document in it.documents) {
                            markets.add(
                                MarketFE(
                                    document.id,
                                    document.get("name").toString(),
                                    document.get("address").toString(),
                                    document.get("latitude").toString().toDouble(),
                                    document.get("longitude").toString().toDouble(),
                                    document.get("concessionaires") as MutableList<String>
                                )
                            )
                        }

                        subscriber.onNext(markets)
                    }.addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }
}