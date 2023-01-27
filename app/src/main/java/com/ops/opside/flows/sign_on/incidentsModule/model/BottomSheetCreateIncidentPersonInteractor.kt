package com.ops.opside.flows.sign_on.incidentsModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.SP_FOREIGN_CONCE_ROLE
import com.ops.opside.common.entities.SP_NORMAL_CONCE_ROLE
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.IncidentFE
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.entities.share.MarketSE
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

    fun insertIncident(incidentPersonFE: IncidentPersonFE): Observable<Boolean> {
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

    fun getConcessByMarket(market: String): Observable<MutableList<ConcessionaireSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val concessionaires = mutableListOf<ConcessionaireSE>()
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .whereArrayContains("participatingMarkets", market)
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            concessionaires.add(
                                ConcessionaireSE(
                                    idFirebase = document.id,
                                    name = document.get("name").toString(),
                                    imageURL = document.get("imageURL").toString(),
                                    address = document.get("address").toString(),
                                    phone = document.get("phone").toString(),
                                    email = document.get("email").toString(),
                                    role = if (document.get("isForeigner").toString().toBoolean())
                                        SP_FOREIGN_CONCE_ROLE else SP_NORMAL_CONCE_ROLE,
                                    lineBusiness = document.get("lineBusiness").toString(),
                                    absence = document.get("absence").toString().toInt(),
                                    isForeigner = document.get("isForeigner").toString()
                                        .toBoolean(),
                                    origin = document.get("origin").toString()
                                )
                            )
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
                        //val sortedByName = marketsList.sortedBy { myObject -> myObject.name }
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

    fun getIncidentList(): Observable<MutableList<IncidentSE>> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                val incidentList = mutableListOf<IncidentSE>()

                firestore.collection(TablesEnum.Incident.getName())
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            incidentList.add(IncidentSE(
                                idFirebase = document.id,
                                incidentName = document.data!!["incidentName"].toString(),
                                incidentPrice = document.data!!["incidentPrice"].toString().toDouble(),
                                incidentDescription = document.data!!["incidentDescription"].toString()
                            ))
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