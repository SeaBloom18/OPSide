package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE
import com.ops.opside.common.entities.DB_TABLE_PARTICIPATING_CONCESS
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.room.TaxCollectionDataBase
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_PRICE_LINEAR_METER
import com.ops.opside.common.utils.getName
import io.reactivex.Observable
import javax.inject.Inject

class TaxCollectionInteractor @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val room: TaxCollectionDataBase,
    private val preferences: Preferences
) {

    fun getConcessionairesFEList(): Observable<MutableList<ConcessionaireFE>> {
        return Observable.unsafeCreate { subscriber ->
            val concessionaires: MutableList<ConcessionaireFE> = mutableListOf()

            firestore.collection(TablesEnum.Concessionaire.getName())
                .get()
                .addOnSuccessListener {

                    for (document in it.documents) {

                        concessionaires.add(
                            ConcessionaireFE(
                                document.id,
                                document.get("name").toString(),
                                document.get("imageURL").toString(),
                                document.get("address").toString(),
                                document.get("origin").toString(),
                                document.get("phone").toString(),
                                document.get("email").toString(),
                                // TODO: Qué pasó con este campo?
                                //document.get("role") as Int,
                                0,
                                document.get("linearMeters").toString().toDouble(),
                                document.get("lineBusiness").toString(),
                                0,
                                document.get("isForeigner").toString().toBoolean(),
                                document.get("password").toString(),
                                document.get("participatingMarkets") as MutableList<String>
                            )
                        )

                    }

                    subscriber.onNext(concessionaires)
                }.addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

    fun getParticipatingConcessList(idMarket: String): Observable<MutableList<ParticipatingConcessSE>> {
        return Observable.unsafeCreate { subscriber ->
            val participatingConcess: MutableList<ParticipatingConcessSE> = mutableListOf()

            firestore.collection(TablesEnum.ParticipatingConcess.getName())
                .whereEqualTo("idMarket", idMarket)
                .get()
                .addOnSuccessListener {

                    for (document in it.documents) {
                        participatingConcess.add(
                            ParticipatingConcessSE(
                                idMarket,
                                document.get("idConcessionaire").toString(),
                                document.id,
                                document.get("linearMeters").toString().toDouble(),
                                document.get("lineBusiness").toString(),
                                document.get("marketName").toString()
                            )
                        )
                    }

                    subscriber.onNext(participatingConcess)
                }.addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

    fun getPersistedParticipatingConcessList(idMarket: String): Observable<MutableList<ParticipatingConcessSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val participatingConcessList =
                    room.concessionaireDao().getAllParticipatingConcessById(idMarket)
                subscriber.onNext(participatingConcessList)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun persistConcessionairesSEList(
        idMarket: String,
        concessionaires: MutableList<ConcessionaireFE>,
        participatingConcessSE: MutableList<ParticipatingConcessSE>
    )
            : Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {

                // Extraemos los ids de los concesionarios pertenecientes al tianguis
                // antes de borrar la relación
                val idsConcessionaires = room.concessionaireDao()
                    .getAllConcessionairesByMarket(idMarket).concessionairetIdList

                //Eliminamos la relación que existe entre el concesionario y el tianguis
                room.concessionaireDao().deleteConcessPartipating(idMarket)

                // Eliminar a los concesionarios para volverlos a insertar
                // y así actualizar cualquier posible cambio
                room.concessionaireDao().deleteConcessInMarket(idsConcessionaires)

                // Volvemos a añadir a los concesionarios con la nueva información
                room.concessionaireDao()
                    .addConcessionairesList(concessionaires.map { it.parseToSE() }.toMutableList())

                // Volvemos a crear las relaciones
                room.concessionaireDao().addConcessPartipating(participatingConcessSE)

                subscriber.onNext(true)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }

    }

    fun getAllConcessionaires(): Observable<MutableList<ConcessionaireSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val concessionaires = room.concessionaireDao().getAllConcessionaires()
                subscriber.onNext(concessionaires)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }


    fun persistMarket(market: MarketFE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                if (room.marketDao().existMarket(market.idFirebase) == null)
                    room.marketDao().addMarket(market.parseToSE())

                subscriber.onNext(true)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun getPriceLinearMeter(): Float {
        return preferences.getFloat(SP_PRICE_LINEAR_METER)
    }

    fun createTaxCollection(taxCollection: TaxCollectionSE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                room.taxCollectionDao().addTaxCollection(taxCollection)
                subscriber.onNext(true)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun getOpenedTaxCollection(idMarket: String): Observable<TaxCollectionSE?> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val taxCollection = room.taxCollectionDao().getOpenedCollection(idMarket)
                subscriber.onNext(taxCollection)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun updateTaxCollection(taxCollection: TaxCollectionSE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                room.taxCollectionDao().updateTaxCollection(taxCollection)
                subscriber.onNext(true)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun createEvent(event: EventRE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                subscriber.onNext(room.eventDao().createEvent(event) == null)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun revertRelatedConcess(idFirebase: String): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.ParticipatingConcess.getName())
                    .document(idFirebase)
                    .delete()
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                        subscriber.onNext(false)
                    }
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    fun deleteTaxCollection(taxCollection: TaxCollectionSE): Pair<Boolean, String> {
        return try {
            room.taxCollectionDao().deleteTaxCollection(taxCollection)
            Pair(true, "Success")
        } catch (exception: Exception) {
            Pair(false, exception.message.toString())
        }
    }

}