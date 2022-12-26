package com.ops.opside.flows.sign_on.concessionaireModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE
import com.ops.opside.common.entities.SP_FOREIGN_CONCE_ROLE
import com.ops.opside.common.entities.SP_NORMAL_CONCE_ROLE
import com.ops.opside.common.entities.share.ConcessionaireSE
import io.reactivex.Observable
import javax.inject.Inject

class ConcessionaireInteractor @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getConcessionairesList(): Observable<MutableList<ConcessionaireSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val concessionaires = mutableListOf<ConcessionaireSE>()
                firestore.collection(DB_TABLE_CONCESSIONAIRE)
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

    fun getConcessByMarketList(markets: MutableList<String>): Observable<MutableList<ConcessionaireSE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val concessionaires = mutableListOf<ConcessionaireSE>()
                firestore.collection(DB_TABLE_CONCESSIONAIRE)
                    .whereArrayContainsAny("participatingMarkets", markets.map { it }.toList())
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