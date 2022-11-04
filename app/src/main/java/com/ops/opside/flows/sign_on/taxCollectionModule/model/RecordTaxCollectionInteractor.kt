package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.room.TaxCollectionDataBase
import io.reactivex.Observable
import javax.inject.Inject

class RecordTaxCollectionInteractor @Inject constructor(
    val room: TaxCollectionDataBase
){

    fun getEventsList(idTaxCollection: String): Observable<MutableList<EventRE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val events = room.eventDao().getAllEvents(idTaxCollection)

                subscriber.onNext(events)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun deleteEvent(event: EventRE): Observable<Boolean>{
        return Observable.unsafeCreate{ subscriber ->
            try {
                val events = room.eventDao().deleteEvent(event)

                subscriber.onNext(true)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

}