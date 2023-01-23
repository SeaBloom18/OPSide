package com.ops.opside.flows.sign_on.incidentsModule.model

import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.entities.share.IncidentSE
import io.reactivex.Observable

class IncidentsInteractor {
    fun getIncidentsPersonsList(): Observable<MutableList<IncidentPersonFE>> {

        return Observable.unsafeCreate{ subscriber ->
            try {
                val incident = mutableListOf<IncidentPersonFE>()

                //val incident1 = IncidentPersonFE("", "David Gonzalez", "Mario Razo", "03/08/2022", 7, 76.0)
                //val incident2 = IncidentPersonFE("", "Alejandro Quezada", "Mario Razo", "08/03/2022", 87, 54.0)

                //incident.add(incident1)
                //incident.add(incident2)
                //incident.add(incident2)

                subscriber.onNext(incident)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}