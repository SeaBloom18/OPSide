package com.ops.opside.flows.sign_on.taxCollectionModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_EVENT
import com.ops.opside.common.entities.DB_TABLE_TAX_COLLECTION
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.room.TaxCollectionDataBase
import io.reactivex.Observable
import javax.inject.Inject

class FinalizeTaxCollectionInteractor @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val room: TaxCollectionDataBase
) {

    fun insertTaxCollection(taxCollection: TaxCollectionSE): Observable<String> {
        return Observable.unsafeCreate { subscriber ->
            firestore.collection(DB_TABLE_TAX_COLLECTION)
                .add(taxCollection.getHashMap())
                .addOnSuccessListener {
                    subscriber.onNext(it.id)
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

    fun insertAllEvents(events: MutableList<EventRE>, idTaxCollection: String): Observable<String> {
        return Observable.unsafeCreate { subscriber ->
            try {
                var breakLoop = false
                events.forEach { event ->
                    event.idTaxCollection = idTaxCollection
                    firestore.collection(DB_TABLE_EVENT)
                        .add(event.getHashMap())
                        .addOnSuccessListener {
                            room.eventDao().deleteEvent(event)
                        }
                        .addOnFailureListener {
                            subscriber.onError(it)
                            breakLoop = true
                        }

                    if (breakLoop)
                        return@forEach
                }
                subscriber.onNext("Success")
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun getEventsList(idTaxCollection: String): MutableList<EventRE> {
        val events = room.eventDao().getAllEvents()
        Log.d("Demo",events.toString())
       return room.eventDao().getAllEventsById(idTaxCollection)
    }

    fun updateEvent(event: EventRE) =
        room.eventDao().updateEvent(event)

    fun updateTaxCollectionId(
        taxCollection: TaxCollectionSE,
        idTaxCollection: String
    ): Pair<Boolean, String> {
        return try {
            taxCollection.idFirebase = idTaxCollection
            room.taxCollectionDao().updateTaxCollection(taxCollection)

            Pair(true, "Success")
        } catch (exception: Exception) {
            Pair(false, exception.message.toString())
        }
    }

    fun searchIfExistTaxCollection(idTaxCollection: String): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            firestore.collection(DB_TABLE_TAX_COLLECTION).document(idTaxCollection).get()
                .addOnSuccessListener {
                    subscriber.onNext(it.exists())
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

    fun closeTaxCollection(taxCollection: TaxCollectionSE): Pair<Boolean,String> {
        return try {
            room.taxCollectionDao().deleteTaxCollectionByMarket(taxCollection.idMarket)
            Pair(true,"Success")
        } catch (e: Exception){
            Pair(false,e.message.toString())
        }
    }

}