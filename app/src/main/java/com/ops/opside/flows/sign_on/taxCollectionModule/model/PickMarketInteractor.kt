package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.ops.opside.common.entities.share.TianguisSE
import io.reactivex.Observable


class PickMarketInteractor {
    fun getMarketsList(): Observable<MutableList<TianguisSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val markets: MutableList<TianguisSE> = mutableListOf()
                for (i in 1..10){
                    markets.add(
                        TianguisSE(
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