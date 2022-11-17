package com.ops.opside.flows.sign_on.dashboardModule.viewModel

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dashboardModule.model.BottomSheetUserProfileInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetUserProfileViewModel @Inject constructor(
    private val mBottomSheetUserProfileInteractor: BottomSheetUserProfileInteractor):
    CommonViewModel(){

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        mBottomSheetUserProfileInteractor.showPersonalInfo()

    fun showAboutInfo(): Pair<String?, Boolean?> =
        mBottomSheetUserProfileInteractor.showAboutInfo()

    fun uploadUserImage(uri: Uri) {
        mBottomSheetUserProfileInteractor.uploadUserImage(uri)
    }
}