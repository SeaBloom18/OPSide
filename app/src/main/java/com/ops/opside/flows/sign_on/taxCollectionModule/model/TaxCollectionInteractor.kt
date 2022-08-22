package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import io.reactivex.Observable

class TaxCollectionInteractor {
    private val firestore = FirebaseFirestore.getInstance()

    fun getConcessionairesFEList(marketId: String): Observable<MutableList<ConcessionaireFE>> {
        return Observable.unsafeCreate { subscriber ->
            val concessionaires: MutableList<ConcessionaireFE> = mutableListOf()

            firestore.collection("concessionaires")
                .whereArrayContains("participatingMarkets", marketId)
                .get()
                .addOnSuccessListener {

                    for (document in it.documents) {
                        concessionaires.add(
                            ConcessionaireFE(
                                document.id,
                                "${document.get("name").toString()} ${document.get("lastName").toString()}",
                                document.get("address").toString(),
                                document.get("origin").toString(),
                                document.get("phone").toString(),
                                document.get("email").toString(),
                                document.get("linearMeters").toString().toDouble(),
                                document.get("lineBusiness").toString(),
                                0,
                                document.get("isForeigner").toString().toBoolean(),
                                document.get("password").toString(),
                                document.get("participatingMarkets") as MutableList<String>
                            )
                        )
                    }

                    subscriber.onNext(concessionaires)
                }.addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }
}