package com.ops.opside.flows.sign_on.dashboardModule.model

import android.net.Uri
import android.os.Build
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ops.opside.common.entities.LINK_COLLECTOR_FOLDER
import com.ops.opside.common.utils.*
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 02/11/22.
 */

class BottomSheetUserProfileInteractor @Inject constructor(
    private val sp: Preferences,
    private var firebaseStorage: FirebaseStorage) {

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        Triple(sp.getString(SP_NAME), sp.getString(SP_EMAIL), sp.getString(SP_PHONE))

    fun showAboutInfo(): Pair<String?, Boolean?> =
        Pair(sp.getString(SP_ADDRESS), sp.getBoolean(SP_HAS_ACCESS))

    fun uploadUserImage(uri: Uri) {
        firebaseStorage = FirebaseStorage.getInstance(LINK_COLLECTOR_FOLDER).reference

        val uploadTask = firebaseStorage.child("opsUserProfile/{$uri.conce}").putFile(uri)

        uploadTask.addOnSuccessListener {
            firebaseStorage.child("opsUserProfile/{$uri}").downloadUrl.addOnSuccessListener {
                updateImageURL(it.toString())
                //toast("si")
            }.addOnFailureListener {
                //toast("no")
            }
        }


    }

    private fun updateImageURL(url: String) {

    }
}