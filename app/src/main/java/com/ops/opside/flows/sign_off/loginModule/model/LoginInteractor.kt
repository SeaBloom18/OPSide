package com.ops.opside.flows.sign_off.loginModule.model

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.Constants
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_IS_INITIALIZED
import io.reactivex.Observable
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val sp: Preferences,
    private val firestore: FirebaseFirestore) {

    private lateinit var password: String

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
                Log.d("password", password.toString())
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }

}