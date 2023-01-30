package com.ops.opside.flows.sign_on.incidentsModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_ID
import com.ops.opside.common.utils.getName
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by davidgonzalez on 30/01/23
 */
class IncidentsAssignedInteractor @Inject constructor(
    private val firestore: FirebaseFirestore, private val preferences: Preferences) {

    fun getIncidentsAssigned(): Observable<MutableList<IncidentPersonFE>> {
        return Observable.unsafeCreate { subscriber ->
            val incidentAssignedList = mutableListOf<IncidentPersonFE>()
            tryOrPrintException {
                firestore.collection(TablesEnum.IncidentPerson.getName())
                    .whereEqualTo("idCollector", preferences.getString(SP_ID))
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            val timestamp = document.data!!["date"] as com.google.firebase.Timestamp
                            val date = timestamp.toDate()
                            incidentAssignedList.add(IncidentPersonFE(
                                idFirebase = document.id,
                                idCollector = document.data!!["idCollector"].toString(),
                                reportName = document.data!!["reportName"].toString(),
                                assignName = document.data!!["assignName"].toString(),
                                date = date.toString(),
                                idIncident = document.data!!["idIncident"].toString(),
                                price = document.data!!["price"] as Double,
                                idTaxCollection = document.data!!["idTaxCollection"].toString(),
                                idConcessionaire = document.data!!["idConcessionaire"].toString()
                            ))
                        }
                        subscriber.onNext(incidentAssignedList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }
}