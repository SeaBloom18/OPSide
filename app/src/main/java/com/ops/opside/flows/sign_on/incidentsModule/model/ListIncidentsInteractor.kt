package com.ops.opside.flows.sign_on.incidentsModule.model

import com.ops.opside.common.entities.share.IncidentSE
import io.reactivex.Observable

class ListIncidentsInteractor {

    fun getIncidentsList(): Observable<MutableList<IncidentSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val incidents = mutableListOf<IncidentSE>()

                val incident1 = IncidentSE(1, "Inasistencia 1", "", 15.0)
                val incident2 = IncidentSE(1, "Inasistencia 2", "", 15.0)

                incidents.add(incident1)
                incidents.add(incident2)

                subscriber.onNext(incidents)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}