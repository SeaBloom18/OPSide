package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence
import io.reactivex.Observable
import javax.inject.Inject

class FinalizeTaxCollectionInteractor @Inject constructor(

){
    fun getAbsenceList(): Observable<MutableList<ItemAbsence>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val absences: MutableList<ItemAbsence> = mutableListOf()
                for (i in 1..10){
                    absences.add(ItemAbsence("$i", "David Gonzales", "example@gmail.com"))
                }

                subscriber.onNext(absences)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}