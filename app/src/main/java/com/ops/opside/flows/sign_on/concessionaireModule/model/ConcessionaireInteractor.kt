package com.ops.opside.flows.sign_on.concessionaireModule.model

import com.ops.opside.common.entities.share.ConcessionaireSE
import io.reactivex.Observable

class ConcessionaireInteractor {

    fun getConcessionaires(): Observable<MutableList<ConcessionaireSE>> {

        return Observable.unsafeCreate { subscriber ->
            try {
                val concessionaires = mutableListOf<ConcessionaireSE>()

                val concessionaire = ConcessionaireSE(1, "1", "David Gonzalez","Direccion de muestra 1", "3328411633", "dagq117@gmail.com", 0.0, "", 0, false)
                concessionaires.add(concessionaire)

                subscriber.onNext(concessionaires)

            }catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}