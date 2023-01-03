package com.ops.opside.flows.sign_on.taxCollectionModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.getName
import io.reactivex.Observable
import javax.inject.Inject

class BottomSheetForeignerAttendanceInteractor @Inject constructor(
    val firestore: FirebaseFirestore
) {
    fun getEmailInformation(email: String): Observable<ConcessionaireSE> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener {
                        try {
                            val result = it.documents[0]
                            val concessionaire = ConcessionaireSE(
                                idFirebase = result.id,
                                name = result.get("name").toString(),
                                address = result.get("address").toString(),
                                phone = result.get("phone").toString(),
                                email = result.get("email").toString(),
                                role = 0,
                                lineBusiness = "",
                                absence = result.get("absence").toString().toInt(),
                                isForeigner = result.get("isForeigner").toString().toBoolean(),
                                origin = result.get("origin").toString(),
                            )

                            subscriber.onNext(concessionaire)
                        } catch (e: Exception){
                            subscriber.onError(e)
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
    }

    fun registerConcessionaire(concessionaireFE: ConcessionaireFE): Observable<String>{
        return Observable.unsafeCreate{ subscriber ->
            try {
                firestore.collection(TablesEnum.Concessionaire.getName())
                    .add(concessionaireFE.getHashMap())
                    .addOnSuccessListener { documentReference ->
                        subscriber.onNext(documentReference.id)
                    }
                    .addOnFailureListener {
                        subscriber.onNext("")
                    }
            } catch (exception: Exception){
                subscriber.onError(exception)
            }
        }
    }
}