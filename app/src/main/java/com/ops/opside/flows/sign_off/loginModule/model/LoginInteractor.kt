package com.ops.opside.flows.sign_off.loginModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.DB_TABLE_RESOURCES
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.*
import com.ops.opside.common.utils.Formaters.orFalse
import com.ops.opside.common.utils.Formaters.orTrue
import com.ops.opside.common.utils.Formaters.orZero
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 02/11/22.
 */

class LoginInteractor @Inject constructor(
    private val sp: Preferences,
    private val firestore: FirebaseFirestore) {

    fun isSPInitialized() = sp.getBoolean(SP_IS_INITIALIZED)
    fun isRememberMe() = sp.getBoolean(SP_REMEMBER_ME)
    fun updateRememberMe(remember: Boolean) = sp.putValue(SP_REMEMBER_ME,remember)
    fun getLoginSp() = Pair(sp.getString(SP_EMAIL),sp.getString(SP_NAME)!!.split(" ")[0])

    fun initSPForCollector(
        collector: CollectorFE,
        linearMeterPrice: Double,
        rememberMe: Boolean,
        useBiometrics: Boolean
    ): Pair<Boolean,String> {
        return try {
            sp.initPreferences(
                id = collector.idFirebase,
                name =  collector.name,
                urlPhoto = collector.imageURL,
                address = collector.address,
                phone =  collector.phone,
                email = collector.email,
                roll = collector.role,
                linearMeters = linearMeterPrice.toString(),
                hasAccess = collector.hasAccess,
                rememberMe = rememberMe,
                useBiometrics = useBiometrics
            )
            Pair(true,"Success")
        } catch (exception: Exception) {
            Pair(false,exception.message.toString())
        }
    }

    fun initSPForConcessionaire(
        concessionaire: ConcessionaireFE,
        rememberMe: Boolean,
        useBiometrics: Boolean
    ): Pair<Boolean,String> {
        return try {
            sp.initPreferences(
                id = concessionaire.idFirebase,
                name =  concessionaire.name,
                address = concessionaire.address,
                origin = concessionaire.origin,
                phone =  concessionaire.phone,
                email = concessionaire.email,
                roll = if(concessionaire.isForeigner) 1 else 2,
                linearMeters = concessionaire.linearMeters.toString(),
                lineBusiness = concessionaire.lineBusiness,
                rememberMe = rememberMe,
                useBiometrics = useBiometrics
            )
            Pair(true,"Success")
        } catch (exception: Exception) {
            Pair(false,exception.message.toString())
        }
    }

    fun getCollectorByEmail(email: String): Observable<CollectorFE> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_COLLECTOR)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { response ->
                        if (response.documents.size == 0) {
                            subscriber.onNext(CollectorFE())
                        } else {

                            val document = response.documents[0]
                            subscriber.onNext(
                                CollectorFE(
                                    idFirebase = document.id,
                                    name = document.data!!["name"].toString(),
                                    imageURL = document.data!!["imageURL"].toString(),
                                    address = document.data!!["address"].toString(),
                                    phone = document.data!!["phone"].toString(),
                                    email = document.data!!["email"].toString(),
                                    role = document.data!!["role"].toString().toInt().orZero(),
                                    hasAccess = document.data!!["hasAccess"].toString().toBoolean().orFalse(),
                                    password = document.data!!["password"].toString()
                                )
                            )

                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("loginFirestore", "Error getting documents: ", exception)
                        subscriber.onError(exception)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun getConcessionaireByEmail(email: String): Observable<ConcessionaireFE> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_COLLECTOR)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { response ->
                        if (response.documents.size == 0) {
                            subscriber.onNext(ConcessionaireFE())
                        } else {

                            val document = response.documents[0]
                            subscriber.onNext(
                                ConcessionaireFE(
                                    idFirebase = document.id,
                                    name = document.data!!["name"].toString(),
                                    address = document.data!!["address"].toString(),
                                    origin = document.data!!["origin"].toString(),
                                    phone = document.data!!["phone"].toString(),
                                    email = document.data!!["email"].toString(),
                                    role = document.data!!["role"].toString().toInt().orZero(),
                                    linearMeters = document.data!!["linearMeters"].toString()
                                        .toDouble().orZero(),
                                    lineBusiness = document.data!!["lineBusiness"].toString(),
                                    absence = document.data!!["absence"].toString().toInt().orZero(),
                                    isForeigner = document.data!!["isForeigner"].toString()
                                        .toBoolean().orTrue(),
                                    password = document.data!!["password"].toString()
                                )
                            )

                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("loginFirestore", "Error getting documents: ", exception)
                        subscriber.onError(exception)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun getLinearMetersPrice(): Observable<Double> {
        return Observable.unsafeCreate{ subscriber ->
            firestore.collection(DB_TABLE_RESOURCES).document("Ulmp4yMD4noSlOE6IwpX")
                .get()
                .addOnSuccessListener { response ->
                    subscriber.onNext(response.data!!["priceLinealMeter"].toString().toDouble())
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                }
        }
    }

    fun isBiometricsActivated(): Boolean = sp.getBoolean(SP_USE_BIOMETRICS)

    fun setUseBiometrics(useIt: Boolean) = sp.putValue(SP_USE_BIOMETRICS, useIt)
}