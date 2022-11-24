package com.ops.opside.flows.sign_on.taxCollectionModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_ID
import io.reactivex.Observable
import javax.inject.Inject

class ConfirmPasswordInteractor @Inject constructor(
    val firestore: FirebaseFirestore,
    val preferences: Preferences
) {

    fun confirmPassword(password: String): Observable<Pair<Boolean, String>> {
        val userId = preferences.getString(SP_ID).orEmpty()

        return Observable.unsafeCreate { subscriber ->
            firestore.collection(DB_TABLE_COLLECTOR).document(userId)
                .get()
                .addOnSuccessListener {
                    val serverPassword = it.data!!["password"].toString()
                    if (password == serverPassword)
                        subscriber.onNext(Pair(true, "Success"))
                    else
                        subscriber.onNext(Pair(false, "No coincide la contraseña"))
                }
                .addOnFailureListener {
                    subscriber.onNext(Pair(false, it.message.toString()))
                }
        }
    }

}