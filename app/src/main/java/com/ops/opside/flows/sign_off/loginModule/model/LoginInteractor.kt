package com.ops.opside.flows.sign_off.loginModule.model

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.Constants
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_IS_INITIALIZED
import io.reactivex.Observable
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val sp: Preferences,
    private val firestore: FirebaseFirestore) {

    lateinit var password: MutableList<String>

    fun isSPInitialized(): Boolean{
        return sp.getBoolean(SP_IS_INITIALIZED).not()
    }

    fun initSP(){
        sp.initPreferences(
            15.5f,
            "Mario Armando Razo Valenzuela",
            "l8oik7bgrvfde",
            3,
            true,
            true
        )
    }

    /*fun getUserByEmail(): Observable<MutableList<ConcessionaireFE>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                val user: MutableList<ConcessionaireFE> = mutableListOf()

                firestore.collection(Constants.FIRESTORE_CONCESSIONAIRES)
                    .whereEqualTo("email", "dagq117@gmail.com")
                    .get()
                    .addOnSuccessListener {
                        for (document in it.documents) {
                           user.add(
                               ConcessionaireFE(
                                   document.id,
                                   document.get("password").toString(),
                                   document.get("name").toString()
                               )
                           )
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("loginFirestore", "Error getting documents: ", exception)
                        subscriber.onError(exception)
                    }
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }*/

    fun getUserByEmail(): Observable<MutableList<String>> {
        password = mutableListOf()
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(Constants.FIRESTORE_CONCESSIONAIRES)
                    .whereEqualTo("email", "dagq117@gmail.com")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            password.add(document.data["password"].toString())
                            Log.d("loginFirestore", "${document.id} => ${document.data["password"]}")
                            subscriber.onNext(password)
                            Log.d("password", password.toString())
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("loginFirestore", "Error getting documents: ", exception)
                        subscriber.onError(exception)
                    }
                Log.d("password", password.toString())
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

}