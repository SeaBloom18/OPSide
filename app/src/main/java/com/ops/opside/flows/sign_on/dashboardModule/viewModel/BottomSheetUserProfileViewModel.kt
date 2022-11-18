package com.ops.opside.flows.sign_on.dashboardModule.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dashboardModule.model.BottomSheetUserProfileInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetUserProfileViewModel @Inject constructor(
    private val mBottomSheetUserProfileInteractor: BottomSheetUserProfileInteractor):
    CommonViewModel(){

    private val updateImageURL = MutableLiveData<Boolean>()

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        mBottomSheetUserProfileInteractor.showPersonalInfo()

    fun showAboutInfo(): Triple<String?, Boolean?, String?> =
        mBottomSheetUserProfileInteractor.showAboutInfo()

    fun uploadUserImage(uri: Uri) {
        mBottomSheetUserProfileInteractor.uploadUserImage(uri)
    }

    /*fun updateImageURL(idFirestore: String, imageURL: String) {
        disposable.add(
            mBottomSheetUserProfileInteractor.updateImageURL(idFirestore, imageURL).applySchedulers()
                .subscribe(
                    {
                        updateImageURL.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }*/
}