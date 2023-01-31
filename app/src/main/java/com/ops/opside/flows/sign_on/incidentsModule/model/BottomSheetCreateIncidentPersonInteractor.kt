package com.ops.opside.flows.sign_on.incidentsModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.SP_FOREIGN_CONCE_ROLE
import com.ops.opside.common.entities.SP_NORMAL_CONCE_ROLE
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_ID
import com.ops.opside.common.utils.getName
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by davidgonzalez on 22/01/23
 */
class BottomSheetCreateIncidentPersonInteractor @Inject constructor(
    private val firestore: FirebaseFirestore, private val preferences: Preferences) {

    fun insertIncidentPerson(incidentPersonFE: IncidentPersonFE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                firestore.collection(TablesEnum.IncidentPerson.getName())
                    .add(incidentPersonFE.getHashMap())
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                        subscriber.onNext(false)
                    }
            }
        }
    }

    fun getConcessByMarket(market: String): Observable<MutableMap<String, String>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val concessionaires = mutableMapOf<String, String>()
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .whereArrayContains("participatingMarkets", market)
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            concessionaires[document.get("name").toString()] = document.id
                        }
                        subscriber.onNext(concessionaires)
                    }
                    .addOnFailureListener { subscriber.onNext(concessionaires) }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

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

    fun getMarkets(): Observable<MutableMap<String, String>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val marketsList = mutableMapOf<String, String>()

                firestore.collection(TablesEnum.Market.getName())
                    .whereEqualTo("isDeleted", false)
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            marketsList[document.get("name").toString()] = document.id
                        }
                        subscriber.onNext(marketsList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun getIncidentList(): Observable<MutableMap<String, String>> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                val incidentList = mutableMapOf<String, String>()

                firestore.collection(TablesEnum.Incident.getName())
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            incidentList[document.get("incidentName").toString()] = document.id
                        }
                        subscriber.onNext(incidentList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }
}