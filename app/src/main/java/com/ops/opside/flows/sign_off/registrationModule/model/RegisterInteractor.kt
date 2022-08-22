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

    fun registerForeignConcessionaire(concessionaireSE: ConcessionaireSE): Observable<MutableMap<String, Any>>{
        return Observable.unsafeCreate { subscriber ->
            try {
                val foreignConcessionaire: MutableMap<String, Any> = HashMap()

                foreignConcessionaire["name"] = concessionaireSE.name
                foreignConcessionaire["email"] = concessionaireSE.email
                foreignConcessionaire["password"] = concessionaireSE.password

                subscriber.onNext(foreignConcessionaire)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun registerCollector(): Observable<MutableMap<String, Any>>{
        return Observable.unsafeCreate { subscriber ->
            try {
                val registerCollector: MutableMap<String, Any> = HashMap()

                subscriber.onNext(registerCollector)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}