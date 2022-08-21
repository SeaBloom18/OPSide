package com.ops.opside.flows.sign_off.registrationModule.model

import com.ops.opside.common.entities.share.ConcessionaireSE
import io.reactivex.Observable

class RegisterInteractor {

    fun registerConcessionaire(concessionaireSE: ConcessionaireSE): Observable<MutableMap<String, Any>>{

        return Observable.unsafeCreate{ subscriber ->

            try {
                val concessionaire: MutableMap<String, Any> = HashMap()

                concessionaire["name"] = concessionaireSE.name
                concessionaire["address"] = concessionaireSE.address
                concessionaire["phone"] = concessionaireSE.phone
                concessionaire["email"] = concessionaireSE.email
                concessionaire["password"] = concessionaireSE.password

                subscriber.onNext(concessionaire)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }

    }
}