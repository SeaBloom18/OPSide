package com.ops.opside.flows.sign_off.loginModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.*
import com.ops.opside.common.utils.*
import io.reactivex.Observable
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val sp: Preferences,
    private val firestore: FirebaseFirestore) {

    private lateinit var password: String
    private lateinit var userRole: String

    /*fun isSPInitialized(): Boolean{
        return sp.getBoolean(SP_IS_INITIALIZED).not()
    }

    fun isRememberMeChecked(): Pair<Boolean, String?> {
        return Pair (sp.getBoolean(SP_REMEMBER_ME), sp.getString(SP_EMAIL))
    }*/

    fun initSP(email: String): Observable<String>{
        userRole = ""
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_CONCESSIONAIRE)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { conceSucc ->
                        if (conceSucc.documents.size > 0){
                            for (document in conceSucc) {
                                val name = document.data["name"].toString()
                                val idFirestore = document.id
                                userRole = document.data["role"].toString()
                                if (document.data["isForeigner"] == true){
                                    sp.initPreferences(15.5f, name, email, idFirestore,
                                        SP_FOREIGN_CONCE_ROLE, true, true)
                                } else {
                                    sp.initPreferences(15.5f, name, email, idFirestore,
                                        SP_NORMAL_CONCE_ROLE, true, true)
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
                                        val idFirestore = document.id
                                        userRole = document.data["role"].toString()
                                        sp.initPreferences(15.5f, name, email, idFirestore,
                                            SP_COLLECTOR_ROLE, true, true)
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
            } catch (exception: Exception){
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
                        if (collectorSucc.documents.size > 0){
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
                                }
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.w("loginFirestore", "Error getting documents: ", exception)
                        subscriber.onError(exception)
                    }
                Log.d("password", password)
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

}