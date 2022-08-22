package com.ops.opside.flows.sign_off.registrationModule.model

import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import io.reactivex.Observable

class RegisterInteractor {

    fun registerConcessionaire(concessionaireFE: ConcessionaireFE): Observable<MutableMap<String, Any>>{
        return Observable.unsafeCreate{ subscriber ->
            try {
                val concessionaire: MutableMap<String, Any> = HashMap()

                concessionaire["name"] = concessionaireFE.name
                concessionaire["address"] = concessionaireFE.address
                concessionaire["phone"] = concessionaireFE.phone
                concessionaire["email"] = concessionaireFE.email
                concessionaire["password"] = concessionaireFE.password
                concessionaire["isForeigner"] = false
                concessionaire["origin"] = ""
                concessionaire["lineBusiness"] = ""

                subscriber.onNext(concessionaire)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun registerForeignConcessionaire(concessionaireFE: ConcessionaireFE): Observable<MutableMap<String, Any>>{
        return Observable.unsafeCreate { subscriber ->
            try {
                val foreignConcessionaire: MutableMap<String, Any> = HashMap()

                foreignConcessionaire["name"] = concessionaireFE.name
                foreignConcessionaire["email"] = concessionaireFE.email
                foreignConcessionaire["password"] = concessionaireFE.password
                foreignConcessionaire["isForeigner"] = true
                foreignConcessionaire["origin"] = ""
                foreignConcessionaire["phone"] = ""
                foreignConcessionaire["lineBusiness"] = ""

                subscriber.onNext(foreignConcessionaire)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

    fun registerCollector(collectorFE: CollectorFE): Observable<MutableMap<String, Any>>{
        return Observable.unsafeCreate { subscriber ->
            try {
                val collector: MutableMap<String, Any> = HashMap()

                subscriber.onNext(collector)

            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}