package com.ops.opside.flows.sign_on.dashboardModule.model

import android.net.Uri
import android.os.Build
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.LINK_COLLECTOR_FOLDER
import com.ops.opside.common.entities.LINK_CONCESSIONAIRES_STORAGE
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

    fun showAboutInfo(): Triple<String?, Boolean?, String?> =
        Triple(sp.getString(SP_ADDRESS), sp.getBoolean(SP_HAS_ACCESS), sp.getString(SP_USER_URL_PHOTO))

    fun uploadUserImage(uri: Uri) {
        mStorageReference = FirebaseStorage.getInstance(LINK_CONCESSIONAIRES_STORAGE).reference
        val uploadTask = mStorageReference.child("$LINK_COLLECTOR_FOLDER/{$uri.conce}").putFile(uri)


        uploadTask.addOnSuccessListener {
            mStorageReference.child("opsUserProfile/CollectorUserPhotos/{$uri}").downloadUrl.addOnSuccessListener {
                //toast("si")
            }.addOnFailureListener {
                //toast("no")
            }
        }
    }

    fun updateImageURL(url: String) {
        tryOrPrintException {
            firestore.collection(DB_TABLE_COLLECTOR).document(sp.getString(SP_ID).toString()).update("imageURL", url)
                .addOnSuccessListener {
                    sp.putValue(SP_USER_URL_PHOTO, url)
                    Log.d("FireStoreDelete", "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener {
                    Log.w("FireStoreDelete", "Error updating document", it)
                }
        }
    }
}