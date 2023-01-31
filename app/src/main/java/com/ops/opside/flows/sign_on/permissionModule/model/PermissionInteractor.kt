package com.ops.opside.flows.sign_on.permissionModule.model

import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.Modul
import com.ops.opside.common.entities.firestore.ModulsTree
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.getName
import io.reactivex.Observable
import javax.inject.Inject

data class PermissionInteractor @Inject constructor(
    private val firestore: FirebaseFirestore, private val sp: Preferences
) {

    fun getAllRoles(): Observable<MutableMap<String,ModulsTree>> {
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Rol.getName())
                    .get()
                    .addOnSuccessListener {
                        val roles: MutableMap<String,ModulsTree> = mutableMapOf()
                        for (document in it.documents){
                            val modul = ModulsTree().fromJson(document.get("tree").toString())
                            modul.idFirebase = document.id
                            modul.numberRol = document.get("numberRol").toString().toInt()
                            roles[modul.nameRol] = modul
                        }

                        subscriber.onNext(roles)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (e: Exception){
                subscriber.onError(e)
            }
        }
    }

    fun createNewRol(rol: ModulsTree): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Rol.getName())
                    .add(rol.getHashMap())
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (e: Exception){
                subscriber.onError(e)
            }
        }
    }

    fun updateRol(rol: ModulsTree): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
            try {
                firestore.collection(TablesEnum.Rol.getName()).document(rol.idFirebase)
                    .update("tree",rol.toJson())
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            } catch (e: Exception){
                subscriber.onError(e)
            }
        }
    }
}