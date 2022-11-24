package com.ops.opside.flows.sign_on.taxCollectionModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_PARTICIPATING_CONCESS
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.room.TaxCollectionDataBase
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject


class RelateConcessMarketInteractor @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val room: TaxCollectionDataBase
) {

    fun relateConcessWithMarket(participatingConcess: ParticipatingConcessRE): Observable<String> {
        return Observable.unsafeCreate { subscriber ->

            tryOrPrintException {
                firestore.collection(DB_TABLE_PARTICIPATING_CONCESS)
                    .add(participatingConcess.getHashMap())
                    .addOnSuccessListener { documentReference ->
                        subscriber.onNext(documentReference.id)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }

        }
    }

    fun persistParticipatingConcess(participatingConcess: ParticipatingConcessRE): Observable<Boolean>{
        return Observable.unsafeCreate{ subscriber ->
            try {
                room.participatingConcessDao().addConcessionaireToMarket(participatingConcess)
                subscriber.onNext(true)
            } catch (e: Exception){
                subscriber.onError(e)
            }
        }
    }

}