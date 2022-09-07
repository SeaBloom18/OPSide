package com.ops.opside.flows.sign_off.loginModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_IS_INITIALIZED
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val sp: Preferences,
    private val firestore: FirebaseFirestore) {

    private lateinit var password: String

    fun isSPInitialized(): Boolean{
        return sp.getBoolean(SP_IS_INITIALIZED).not()
    }
    /*
            * 1.- una vez sabiendo que el login fue exitoso con el mismo correo hacer una consulta
            * para saber si es que tipo de usuario es
            * 2.- consultar a la coleccion de concessionarios y el tamaño es mayor a 0 signfica que
            * pertenece a esa coleccion en caso de que no solo queda la opcion de que sea de la tabla
            * collector por lo que se hace la consulta a esa coleccion en base al correo
            * 3.- hacer una consulta a la coleccion donde esta el precio lineal
            * 4.- llenar la funcion initSP con todos los datos solo en caso de que se login sea success
    */

    fun initSP(email: String){
        tryOrPrintException {
            firestore.collection(DB_TABLE_CONCESSIONAIRE)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener {
                    if (it.documents.size > 0){
                        for (document in it) {
                            val name = document.data["name"].toString()
                            val idFirestore = document.data["idFirebase"].toString()
                            //val role = document.data["role"] as Int
                            //val hasAccess = document.data["hasAccess"] as Boolean
                            sp.initPreferences(15.5f, name, email, idFirestore, 1, true)
                        }
                    } else {
                        firestore.collection(DB_TABLE_COLLECTOR)
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener {
                                for (document in it) {
                                    val name = document.data["name"].toString()
                                    val idFirestore = document.data["idFirebase"].toString()
                                    //val role = document.data["role"] as Int
                                    //val hasAccess = document.data["hasAccess"] as Boolean
                                    sp.initPreferences(15.5f, name, email, idFirestore, 1, true)
                                }
                            }
                            .addOnFailureListener {
                                Log.e("initSPError", it.toString())
                            }
                    }
                }
                .addOnFailureListener {
                    Log.e("initSPError", it.toString())
                }
        }

        /*sp.initPreferences(
            15.5f,
            "Mario Armando Razo Valenzuela",
            "l8oik7bgrvfde",
            "",
            5,
            true
        )*/
    }

    fun getDataIfLoginSuccess(email: String): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->

            tryOrPrintException {
                firestore.collection(DB_TABLE_CONCESSIONAIRE)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener {
                        subscriber.onNext(it.documents.size > 0)

                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }

    fun getUserByEmail(email: String): Observable<String> {
        password = ""
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(DB_TABLE_CONCESSIONAIRE)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            password = document.data["password"].toString()
                            Log.d("loginFirestore", "${document.id} => ${document.data["password"]}")
                            subscriber.onNext(password)
                            Log.d("password", password)
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