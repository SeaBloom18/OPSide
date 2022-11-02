package com.ops.opside.flows.sign_on.concessionaireModule.model

import com.ops.opside.common.entities.share.MarketSE
import io.reactivex.Observable
import javax.inject.Inject

class ConcessionaireCrudInteractor @Inject constructor(

){
    fun getMarketsList(): Observable<MutableList<MarketSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val markets = mutableListOf<MarketSE>()

                for (i in 1..7){
                    markets.add(MarketSE(i.toString(), "Tianguis $i", "",
                        0.0,0.0,""))
                }

                subscriber.onNext(markets)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}