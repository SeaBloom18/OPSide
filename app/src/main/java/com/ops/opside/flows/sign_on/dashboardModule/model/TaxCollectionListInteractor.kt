package com.ops.opside.flows.sign_on.dashboardModule.model

import io.reactivex.Observable
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.*
import javax.inject.Inject

/**
 * Created by davidgonzalez on 15/01/23
 */
class TaxCollectionListInteractor @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preferences: Preferences
) {

    fun getTaxCollections(): Observable<MutableList<TaxCollectionSE>> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                val taxCollectionList = mutableListOf<TaxCollectionSE>()

                firestore.collection(TablesEnum.TaxCollection.getName())
                    .whereEqualTo("idTaxCollector", preferences.getString(SP_ID))
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            taxCollectionList.add(
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
                                    idTaxCollector = document.data!!["idTaxCollector"].toString()
                                )
                            )
                        }
                        subscriber.onNext(taxCollectionList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }
}