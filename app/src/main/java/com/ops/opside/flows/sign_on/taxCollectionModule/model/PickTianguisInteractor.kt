package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.ops.opside.common.entities.share.TianguisSE
import io.reactivex.Observable


class PickTianguisInteractor {
    fun getTianguisList(): Observable<MutableList<TianguisSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val elements: MutableList<TianguisSE> = mutableListOf()
                for (i in 1..10){
                    elements.add(
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

                subscriber.onNext(elements)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}