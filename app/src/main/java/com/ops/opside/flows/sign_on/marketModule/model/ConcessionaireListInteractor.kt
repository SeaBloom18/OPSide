package com.ops.opside.flows.sign_on.marketModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 11/10/22.
 */
class ConcessionaireListInteractor @Inject constructor(private val firestore: FirebaseFirestore){

    fun getConcessionaireList(marketId: String): Observable<String>{
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                //Log.d("marketId", marketId)
                subscriber.onNext(marketId)
            }
        }
    }
}