package com.ops.opside.flows.sign_on.dashboardModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 10/11/22.
 */
data class ControlPanelInteractor @Inject constructor(private val firebase: FirebaseFirestore) {

    fun getCollectors(): Observable<MutableList<CollectorSE>> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                val collectorList = mutableListOf<CollectorSE>()

                firebase.collection(DB_TABLE_COLLECTOR)
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            collectorList.add(CollectorSE(
                                idFirebase = document.id,
                                name = document.get("name").toString(),
                                address = document.get("address").toString(),
                                hasAccess = document.get("hasAccess") as Boolean,
                                email = document.get("email").toString(),
                                phone = document.get("phone").toString()))
                        }
                        subscriber.onNext(collectorList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }
}