package com.ops.opside.flows.sign_on.incidentsModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.IncidentFE
import com.ops.opside.common.utils.getName
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by davidgonzalez on 23/01/23
 */
class BottomSheetCreateIncidentInteractor @Inject constructor(
    private val firestore: FirebaseFirestore) {

    fun insertIncident(incidentFE: IncidentFE): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                firestore.collection(TablesEnum.Incident.getName())
                    .add(incidentFE.getHashMap())
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                        subscriber.onNext(false)
                    }
            }
        }
    }
}