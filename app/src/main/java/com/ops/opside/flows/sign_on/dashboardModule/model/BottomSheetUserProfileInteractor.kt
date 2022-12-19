package com.ops.opside.flows.sign_on.dashboardModule.model

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.LINK_FIREBASE_STORAGE
import com.ops.opside.common.entities.PATH_COLLECTOR_REFERENCE
import com.ops.opside.common.utils.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 02/11/22.
 */

class BottomSheetUserProfileInteractor @Inject constructor(
    private val sp: Preferences, private val firestore: FirebaseFirestore) {

    private lateinit var mStorageReference: StorageReference

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        Triple(sp.getString(SP_NAME), sp.getString(SP_EMAIL), sp.getString(SP_PHONE))

    fun showAboutInfo(): Pair<String?, String?> =
        Pair(sp.getString(SP_ADDRESS), sp.getString(SP_USER_URL_PHOTO))

    fun uploadUserImage(uri: Uri): Observable<Uri> {
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                mStorageReference = FirebaseStorage.getInstance(LINK_FIREBASE_STORAGE).reference
                val uploadTask =
                    mStorageReference.child("$PATH_COLLECTOR_REFERENCE{${uri}}").putFile(uri)

                uploadTask.addOnSuccessListener {
                    mStorageReference.child("$PATH_COLLECTOR_REFERENCE{$uri}").downloadUrl.addOnSuccessListener {
                        subscriber.onNext(it)
                    }.addOnFailureListener {
                        subscriber.onError(it)
                    }
                }
            }
        }
    }

    fun deleteUserImage() {
        tryOrPrintException {
            val deleteFile = FirebaseStorage.getInstance().getReferenceFromUrl(sp.getString(
                SP_USER_URL_PHOTO).toString())
            deleteFile.delete().addOnSuccessListener {
                Log.d("StorageUserProfilePhotoDeletedSuccess", "DocumentSnapshot successfully deleted!")
            }.addOnFailureListener {
                Log.w("StorageUserProfilePhotoDeletedError", "Error deleting document", it)
            }
        }
    }

    fun updateImageURL(url: String){
        tryOrPrintException {
            firestore.collection(DB_TABLE_COLLECTOR).document(sp.getString(SP_ID).toString()).update("imageURL", url)
                .addOnSuccessListener {
                    sp.putValue(SP_USER_URL_PHOTO, url)
                    //deleteUserImage()
                    Log.d("StorageUserProfilePhotoUpdatedSuccess", "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener {
                    Log.w("StorageUserProfilePhotoUpdatedError", "Error updating document", it)
                }
        }
    }
}