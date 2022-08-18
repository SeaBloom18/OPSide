package com.ops.opside.flows.sign_on.concessionaireModule.model

import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.TianguisSE
import io.reactivex.Observable

class ConcessionaireInteractor {
    fun getConcessionairesList(): Observable<MutableList<ConcessionaireSE>> {
        return Observable.unsafeCreate{ subscriber ->
            try {
                val concessionaires = mutableListOf<ConcessionaireSE>()

                for (i in 1..30) {
                    concessionaires.add(
                        ConcessionaireSE(
                            i.toLong(), "Concesionario $i",
                            "",
                            "",
                            "",
                            "", 3.0, ""
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