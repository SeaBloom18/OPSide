package com.ops.opside.flows.sign_on.taxCollectionCrudModule.model

import com.ops.opside.common.entities.share.TaxCollectionSE
import io.reactivex.Observable
import javax.inject.Inject

class TaxCollectionCrudInteractor @Inject constructor(

){
    fun getCollectionList(): Observable<MutableList<TaxCollectionSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val collections = mutableListOf<TaxCollectionSE>()

                for (i in 1..15) {
                    collections.add(
                        TaxCollectionSE(
                            i.toString(), "",
                            "Tianguis Minicipal",
                            1250.0,
                            "2022-07-12",
                            "", "", ""
                        )
                    )
                }

                subscriber.onNext(collections)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}