package com.ops.opside.flows.sign_on.concessionaireModule.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_PARTICIPATING_CONCESS
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.utils.getName
import io.reactivex.Observable
import javax.inject.Inject

class ConcessionaireCrudInteractor @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getRelatesList(idConcessionaire: String): Observable<MutableList<ParticipatingConcessSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val markets = mutableListOf<ParticipatingConcessSE>()
                firestore.collection(TablesEnum.ParticipatingConcess.getName())
                    .whereEqualTo("idConcessionaire", idConcessionaire)
                    .get()
                    .addOnSuccessListener {
                        for (docuement in it.documents) {
                            markets.add(
                                ParticipatingConcessSE(
                                    idFirebase = docuement.id,
                                    idMarket = docuement.get("idMarket").toString(),
                                    idConcessionaire = docuement.get("idConcessionaire").toString(),
                                    lineBusiness = docuement.get("lineBusiness").toString(),
                                    linearMeters = docuement.get("linearMeters").toString()
                                        .toDouble(),
                                    marketName = docuement.get("marketName").toString()
                                )
                            )
                        }
                        subscriber.onNext(markets)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun deleteRelate(relation: ParticipatingConcessSE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            firestore.collection(TablesEnum.Concessionaire.getName())
                .document(relation.idConcessionaire)
                .update("participatingMarkets", FieldValue.arrayRemove(relation.idMarket))
                .addOnSuccessListener {
                    firestore.collection(TablesEnum.ParticipatingConcess.getName())
                        .document(relation.idFirebase)
                        .delete()
                        .addOnSuccessListener {
                            subscriber.onNext(true)
                        }
                        .addOnFailureListener {
                            subscriber.onError(it)
                        }
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

    fun getMarkets(): Observable<MutableList<MarketSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val marketsList = mutableListOf<MarketSE>()

                firestore.collection(TablesEnum.Market.getName())
                    .whereEqualTo("isDeleted", false)
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            marketsList.add(
                                MarketSE(
                                    idFirebase = document.id,
                                    name = document.get("name").toString(),
                                    address = document.get("address").toString(),
                                    marketMeters = document.get("marketMeters") as Double,
                                    latitude = document.get("latitude") as Double,
                                    longitude = document.get("longitude") as Double,
                                    numberConcessionaires = listOf(document.get("concessionaires")).toString()
                                )
                            )
                        }
                        subscriber.onNext(marketsList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun addMarketToConcess(idMarket: String, idConcessionaire: String): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            firestore.collection(TablesEnum.Concessionaire.getName()).document(idConcessionaire)
                .update("participatingMarkets", FieldValue.arrayUnion(idMarket))
                .addOnSuccessListener {
                    subscriber.onNext(true)
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }
}