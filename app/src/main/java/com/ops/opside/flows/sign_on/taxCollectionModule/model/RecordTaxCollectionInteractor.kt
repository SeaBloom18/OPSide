package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ADDED
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.FLOOR_COLLECTION
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.PENALTY_FEE
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord
import io.reactivex.Observable

class RecordTaxCollectionInteractor {

    fun getEventsList(): Observable<MutableList<ItemRecord>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val events = mutableListOf<ItemRecord>()

                events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:25 PM", 25.0))
                events.add(ItemRecord(FLOOR_COLLECTION, "Citlaly García Razo", "04:27 PM", 54.5))
                events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:29 PM", 34.0))
                events.add(ItemRecord(ADDED, "Julio Zepeda", "04:30 PM"))
                events.add(ItemRecord(FLOOR_COLLECTION, "Julio Zepeda", "04:31 PM", 34.0))
                events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:32 PM", 25.0))
                events.add(ItemRecord(PENALTY_FEE, "Citlaly García Razo", "04:33 PM"))
                events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:35 PM", 34.0))
                events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:25 PM", 25.0))
                events.add(ItemRecord(FLOOR_COLLECTION, "Citlaly García Razo", "04:27 PM", 54.5))
                events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:29 PM", 34.0))
                events.add(ItemRecord(ADDED, "Julio Zepeda", "04:30 PM"))
                events.add(ItemRecord(FLOOR_COLLECTION, "Julio Zepeda", "04:31 PM", 34.0))
                events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:32 PM", 25.0))
                events.add(ItemRecord(PENALTY_FEE, "Citlaly García Razo", "04:33 PM"))
                events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:35 PM", 34.0))


                subscriber.onNext(events)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}