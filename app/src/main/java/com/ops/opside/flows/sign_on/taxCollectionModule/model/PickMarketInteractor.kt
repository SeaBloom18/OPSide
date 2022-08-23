package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_IS_ON_LINE_MODE
import io.reactivex.Observable
import javax.inject.Inject


class PickMarketInteractor @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sp: Preferences
){

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

    fun putIsOnLineMode(isOnLineMode: Boolean){
        sp.putValue(SP_IS_ON_LINE_MODE, isOnLineMode)
    }

    fun isOnLineMode(): Boolean{
        return sp.getBoolean(SP_IS_ON_LINE_MODE)
    }
}