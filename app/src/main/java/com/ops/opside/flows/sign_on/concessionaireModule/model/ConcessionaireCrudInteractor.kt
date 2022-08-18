package com.ops.opside.flows.sign_on.concessionaireModule.model

import com.ops.opside.common.entities.share.TianguisSE
import io.reactivex.Observable

class ConcessionaireCrudInteractor {
    fun getMarketsList(): Observable<MutableList<TianguisSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val markets = mutableListOf<TianguisSE>()

                for (i in 1..7){
                    markets.add(TianguisSE(i.toLong(), "Tianguis $i", "",
                        "",0.0,0.0,0))
                }

                subscriber.onNext(markets)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}