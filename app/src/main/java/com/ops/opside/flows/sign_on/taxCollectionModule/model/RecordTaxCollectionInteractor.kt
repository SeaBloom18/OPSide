package com.ops.opside.flows.sign_on.taxCollectionModule.model

import android.util.Log
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.room.TaxCollectionDataBase
import io.reactivex.Observable
import javax.inject.Inject

class RecordTaxCollectionInteractor @Inject constructor(
    val room: TaxCollectionDataBase
){

    fun getEventsList(idTaxCollection: String): MutableList<EventRE> =
        room.eventDao().getAllEventsById(idTaxCollection)


    fun hasEvents(idTaxCollection: String): Boolean{
        val events = room.eventDao().getAllEventsById(idTaxCollection)
        return events.size > 0
    }

    fun deleteEvent(event: EventRE): Observable<Boolean>{
        return Observable.unsafeCreate{ subscriber ->
            try {
                room.eventDao().deleteEvent(event)
                subscriber.onNext(true)
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

}