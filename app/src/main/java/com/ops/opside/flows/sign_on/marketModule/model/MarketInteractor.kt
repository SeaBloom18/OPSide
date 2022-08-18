package com.ops.opside.flows.sign_on.marketModule.model

import com.ops.opside.common.entities.share.TianguisSE
import io.reactivex.Observable

class MarketInteractor {

    fun getMarkets(): Observable<MutableList<TianguisSE>> {

        return Observable.unsafeCreate { subscriber ->
            try {
                val markets = mutableListOf<TianguisSE>()

                val market1 = TianguisSE(1, "1", "Tianguis de muestra 1","Direccion de muestra 1",0.0,0.0,73)
                val market2 = TianguisSE(2, "1", "Tianguis de muestra 2","Direccion de muestra 2",0.0,0.0,54)
                val market3 = TianguisSE(3, "1", "Tianguis de muestra 3","Direccion de muestra 3",0.0,0.0,43)

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