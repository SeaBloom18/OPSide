package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.ops.opside.common.entities.share.MarketSE
import io.reactivex.Observable


class PickMarketInteractor {
    fun getMarketsList(): Observable<MutableList<MarketSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val markets: MutableList<MarketSE> = mutableListOf()
                for (i in 1..10){
                    markets.add(
                        MarketSE(
                        (i).toLong(),
                            "",
                            "Tianguis $i",
                            "",
                            0.0,
                            0.0,
                            0
                    )
                    )
                }

                subscriber.onNext(markets)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}