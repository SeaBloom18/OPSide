package com.ops.opside.flows.sign_on.concessionaireModule.model

import com.ops.opside.common.entities.share.ConcessionaireSE
import io.reactivex.Observable
import javax.inject.Inject

class ConcessionaireInteractor @Inject constructor(

){
    fun getConcessionairesList(): Observable<MutableList<ConcessionaireSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val concessionaires = mutableListOf<ConcessionaireSE>()

                for (i in 1..30) {
                    concessionaires.add(
                        ConcessionaireSE(
                            i.toString(), "Concesionario $i",
                            "",
                            "",
                            "",
                            0,
                            "", 0, false
                        )
                    )
                }

                subscriber.onNext(concessionaires)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}