package com.ops.opside.flows.sign_off.loginModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.*
import com.ops.opside.common.utils.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 02/11/22.
 */

class LoginInteractor @Inject constructor(
    private val sp: Preferences,
    private val firestore: FirebaseFirestore
) {

    private lateinit var password: String
    private lateinit var userRole: String

    fun isSPInitialized(): Boolean {
        return sp.getBoolean(SP_IS_INITIALIZED).not()
    }

    fun isRememberMeChecked(): Triple<Boolean, String?, String?> =
        Triple(sp.getBoolean(SP_REMEMBER_ME), sp.getString(SP_EMAIL), sp.getString(SP_NAME))


    fun initSP(email: String, rememberMe: Boolean, useBiometrics: Boolean): Observable<String> {
        userRole = ""
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_CONCESSIONAIRE)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { conceSucc ->
                        if (conceSucc.documents.size > 0) {
                            for (document in conceSucc) {
                                val name = document.data["name"].toString()
                                val phone = document.data["phone"].toString()
                                val origin = document.data["origin"].toString()
                                val address = document.data["address"].toString()
                                val lineBusiness = document.data["lineBusiness"].toString()
                                val absence = document.data["absence"].toString()
                                val linearMeters = document.data["linearMeters"].toString()
                                val userType = document.data[""].toString()
                                val idFirestore = document.id
                                userRole = document.data["role"].toString()
                                if (document.data["isForeigner"] == true) {
                                    sp.initPreferences(
                                        priceLinearMeter = 15.5f,
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        origin = origin,
                                        address = address,
                                        lineBusiness = lineBusiness,
                                        absence = absence,
                                        linearMeters = linearMeters,
                                        userType = origin,
                                        id = idFirestore,
                                        roll = SP_FOREIGN_CONCE_ROLE,
                                        hasAccess = true,
                                        rememberMe = rememberMe,
                                        useBiometrics = useBiometrics
                                    )
                                } else {
                                    sp.initPreferences(
                                        priceLinearMeter = 15.5f,
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        origin = origin,
                                        address = address,
                                        lineBusiness = lineBusiness,
                                        absence = absence,
                                        linearMeters = linearMeters,
                                        userType = origin,
                                        id = idFirestore,
                                        roll = SP_NORMAL_CONCE_ROLE,
                                        hasAccess = true,
                                        rememberMe = rememberMe,
                                        useBiometrics = useBiometrics
                                    )
                                }
                                subscriber.onNext(userRole)
                            }
                        } else {
                            firestore.collection(DB_TABLE_COLLECTOR)
                                .whereEqualTo("email", email)
                                .get()
                                .addOnSuccessListener { collectorSucc ->
                                    for (document in collectorSucc) {
                                        val name = document.data["name"].toString()
                                        val phone = document.data["phone"].toString()
                                        val origin = document.data["origin"].toString()
                                        val address = document.data["address"].toString()
                                        val lineBusiness = document.data["lineBusiness"].toString()
                                        val absence = document.data["absence"].toString()
                                        val linearMeters = document.data["linearMeters"].toString()
                                        val idFirestore = document.id
                                        userRole = document.data["role"].toString()
                                        sp.initPreferences(
                                            priceLinearMeter = 15.5f,
                                            name = name,
                                            email = email,
                                            phone = phone,
                                            origin = origin,
                                            address = address,
                                            lineBusiness = lineBusiness,
                                            absence = absence,
                                            linearMeters = linearMeters,
                                            userType = origin,
                                            id = idFirestore,
                                            roll = SP_COLLECTOR_ROLE,
                                            hasAccess = true,
                                            rememberMe = rememberMe,
                                            useBiometrics = useBiometrics
                                        )
                                        subscriber.onNext(userRole)
                                    }
                                }
                                .addOnFailureListener {
                                    Log.e("initSPError", it.toString())
                                    subscriber.onError(it)
                                }
                        }
                    }
                    .addOnFailureListener {
                        Log.e("initSPError", it.toString())
                        subscriber.onError(it)
                    }
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun getUserByEmail(email: String): Observable<String> {
        password = ""
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_COLLECTOR)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { collectorSucc ->
                        if (collectorSucc.documents.size > 0) {
                            for (document in collectorSucc) {
                                password = document.data["password"].toString()
                                subscriber.onNext(password)
                            }
                        } else {
                            firestore.collection(DB_TABLE_CONCESSIONAIRE)
                                .whereEqualTo("email", email)
                                .get()
                                .addOnSuccessListener { conceSucc ->
                                    for (document in conceSucc) {
                                        password = document.data["password"].toString()
                                        subscriber.onNext(password)
                                    }
                                }
                                .addOnFailureListener {
                                    Log.e("initSPError", it.toString())
                                    subscriber.onError(it)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("loginFirestore", "Error getting documents: ", exception)
                        subscriber.onError(exception)
                    }
                Log.d("password", password)
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    fun isBiometricsActivated(): Boolean = sp.getBoolean(SP_USE_BIOMETRICS)

    fun setUseBiometrics(useIt: Boolean) = sp.putValue(SP_USE_BIOMETRICS, useIt)

    fun getRol() = sp.getInt(SP_ROLL)

    fun insertOrigins() {
        /*val originsString = "Acatic,Acatlán de Juárez,Ahualulco de Mercado,Amacueca,Amatitán,Ameca,San Juanito de Escobedo,Arandas,El Arenal,Atemajac de Brizuela,Atengo,Atenguillo,Atotonilco el Alto,Atoyac,Autlán de Navarro,Ayotlán,Ayutla,La Barca,Bolaños,Cabo Corrientes,Casimiro Castillo,Cihuatlán,Zapotlán el Grande,Cocula,Colotlán,Concepción de Buenos Aire,Cuautitlán de García Barragán,Cuautla,Cuquío,Chapala,Chimaltitán,Chiquilistlán,Degollado,Ejutla,Encarnación de Díaz,Etzatlán,El Grullo,Guachinango,Guadalajara,Hostotipaquillo,Huejúcar,Huejuquilla el Alto,La Huerta,Ixtlahuacán de los Membrillos,Ixtlahuacán del Río,Jalostotitlán,Jamay,Jesús María,Jilotlán de los Dolores,Jocotepec,Juanacatlán,Juchitlán,Lagos de Moreno,El Limón,Magdalena,Santa María del Oro,La Manzanilla de la Paz,Mascota,Mazamitla,Mexticacán,Mezquitic,Mixtlán,Ocotlán,Ojuelos de Jalisco,Pihuamo,Poncitlán,Puerto Vallarta,Villa Purificación,Quitupan,El Salto,San Cristóbal de la Barranca,San Diego de Alejandría,San Juan de los Lagos,San Julián,San Marcos,San Martín de Bolaños,San Martín Hidalgo,San Miguel el Alto,Gómez Farías,San Sebastián del Oeste,Santa María de los Ángeles,Sayula,Tala,Talpa de Allende,Tamazula de Gordiano,Tapalpa,Tecalitlán,Techaluta de Montenegro,Tecolotlán,Tenamaxtlán,Teocaltiche,Teocuitatlán de Corona,Tepatitlán de Morelos,Tequila,Teuchitlán,Tizapán el Alto,Tlajomulco de Zúñiga,San Pedro Tlaquepaque,Tolimán,Tomatlán,Tonalá,Tonaya,Tonila,Totatiche,Tototlán,Tuxcacuesco,Tuxcueca,Tuxpan,Unión de San Antonio,Unión de Tula,Valle de Guadalupe,Valle de Juárez,San Gabriel,Villa Corona,Villa Guerrero,Villa Hidalgo,Cañadas de Obregón,Yahualica de González Gallo,Zacoalco de Torres,Zapopan,Zapotiltic,Zapotitlán de Vadillo,Zapotlán del Rey,Zapotlanejo,San Ignacio Cerro Gordo"
        val origins = originsString.split(",").map { OriginFE(null, it) } as MutableList<OriginFE>

        origins.map {
            firestore.collection(DB_TABLE_ORIGIN)
                .add(it.getHashMap())
        }*/
    }

}