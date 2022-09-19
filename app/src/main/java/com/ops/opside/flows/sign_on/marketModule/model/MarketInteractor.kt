package com.ops.opside.flows.sign_on.marketModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.share.MarketSE
import io.reactivex.Observable
import javax.inject.Inject

class MarketInteractor @Inject constructor(
    private val firestore: FirebaseFirestore){

    fun getMarkets(): Observable<MutableList<MarketSE>> {

        return Observable.unsafeCreate { subscriber ->
            try {
                val markets = mutableListOf<MarketSE>()

                val market1 = MarketSE("1",  "Tianguis de muestra 1",
                    "Direccion de muestra 1",0.0,0.0,73)
                val market2 = MarketSE("2",  "Tianguis de muestra 2",
                    "Direccion de muestra 2",0.0,0.0,54)
                val market3 = MarketSE("3",  "Tianguis de muestra 3",
                    "Direccion de muestra 3",0.0,0.0,43)

                markets.add(market1)
                markets.add(market2)
                markets.add(market3)

                subscriber.onNext(markets)

            }catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}