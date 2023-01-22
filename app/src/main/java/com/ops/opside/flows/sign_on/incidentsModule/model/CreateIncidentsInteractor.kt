package com.ops.opside.flows.sign_on.incidentsModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.SP_FOREIGN_CONCE_ROLE
import com.ops.opside.common.entities.SP_NORMAL_CONCE_ROLE
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.getName
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by davidgonzalez on 22/01/23
 */
class CreateIncidentsInteractor @Inject constructor(private val firestore: FirebaseFirestore) {

    fun getConcessionaireList() : Observable<MutableList<ConcessionaireSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val concessionaires = mutableListOf<ConcessionaireSE>()
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                            concessionaires.add(
                                ConcessionaireSE(
                                    idFirebase = document.id,
                                    name = document.get("name").toString(),
                                    imageURL = document.get("imageURL").toString(),
                                    address = document.get("address").toString(),
                                    phone = document.get("phone").toString(),
                                    email = document.get("email").toString(),
                                    role = if (document.get("isForeigner").toString().toBoolean())
                                        SP_FOREIGN_CONCE_ROLE else SP_NORMAL_CONCE_ROLE,
                                    lineBusiness = document.get("lineBusiness").toString(),
                                    absence = document.get("absence").toString().toInt(),
                                    isForeigner = document.get("isForeigner").toString()
                                        .toBoolean(),
                                    origin = document.get("origin").toString()
                                )
                            )
                        }
                        subscriber.onNext(concessionaires)
                    }
                    .addOnFailureListener { subscriber.onNext(concessionaires) }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }
}