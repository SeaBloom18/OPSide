package com.ops.opside.flows.sign_on.taxCollectionCrudModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_TAX_COLLECTION
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.getName
import io.reactivex.Observable
import javax.inject.Inject

class TaxCollectionCrudInteractor @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getCollectionList(): Observable<MutableList<TaxCollectionSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val collections = mutableListOf<TaxCollectionSE>()

                firestore.collection(TablesEnum.TaxCollection.getName())
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            collections.add(
                                TaxCollectionSE(
                                    idFirebase = document.id,
                                    idMarket = document.data!!["idMarket"].toString(),
                                    marketName = document.data!!["marketName"].toString(),
                                    totalAmount = document.data!!["totalAmount"].toString()
                                        .toDouble(),
                                    startDate = document.data!!["startDate"].toString(),
                                    endDate = document.data!!["endDate"].toString(),
                                    startTime = document.data!!["startTime"].toString(),
                                    endTime = document.data!!["endTime"].toString(),
                                    taxCollector = document.data!!["taxCollector"].toString(),
                                    idTaxCollector = document.data!!["idTaxCollector"].toString(),
                                )
                            )
                        }
                        subscriber.onNext(collections)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }
}