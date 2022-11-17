package com.ops.opside.flows.sign_on.dashboardModule.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ops.opside.BuildConfig
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.utils.toast
import com.ops.opside.databinding.BottomSheetUserProfileBinding
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.BottomSheetUserProfileViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

/**
 * Created by David Alejandro González Quezada on 28/10/22.
 */

@AndroidEntryPoint
class BottomSheetUserProfile : BottomSheetDialogFragment(){

    private val mBinding: BottomSheetUserProfileBinding by lazy {
        BottomSheetUserProfileBinding.inflate(layoutInflater)
    }

    private val mViewModel: BottomSheetUserProfileViewModel by viewModels()
    private val mActivity: MainActivity by lazy { activity as MainActivity }
    private lateinit var mStorageReference: StorageReference


    private val cameraPermission = registerForActivityResult(ActivityResultContracts
        .RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
            }
            permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
            } else -> {
            mActivity.finish()
        }
        }
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                mBinding.ivProfilePicture.setImageURI(uri)
                mBinding.lottieAnimationView.visibility = View.INVISIBLE
                mStorageReference = FirebaseStorage.getInstance("gs://opss-fbd9e.appspot.com").reference

                val uploadTask = mStorageReference.child("opsUserProfile/testName").putFile(uri)
                mBinding.tvUserPhone.text = mStorageReference.child("opsUserProfile/testName").downloadUrl.toString()
                Log.d("imgStorageURL", mStorageReference.child("opsUserProfile/testName").downloadUrl.toString())

                uploadTask.addOnSuccessListener {

                }
            }
        }
    }

    private var latestTmpUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraPermission.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA))
        takeUserPhoto()
        mBinding.apply {
            ivBack.setOnClickListener { dismiss() }
            tvLogOut.setOnClickListener { logOut() }
            ivChangePhoto.setOnClickListener {
                takeImage()
            }
            ivShareProfile.setOnClickListener { shareUserProfile() }
            btnSaveProfile.setOnClickListener {
                mStorageReference = FirebaseStorage.getInstance().reference
                /*tomar foto
                * subir a storage y obtener su url
                * agregar url al nuevo campo de firestore
                * consumir url con glide*/
                //mStorageReference.child()
            }
        }

        /** Methods Calls **/
        showPersonalUserInfo()
    }

    /** Take Photo **/
    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", mActivity.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(mActivity, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    /** Other Methods**/
    private fun showPersonalUserInfo(){
        val userPersonalInfo = mViewModel.showPersonalInfo()
        val userAboutInfo = mViewModel.showAboutInfo()
        with(mBinding){
            tvUserName.text = userPersonalInfo.first
            tvUserEmail.text = userPersonalInfo.second
            tvUserPhone.text = userPersonalInfo.third

            tvUserProfileAdress.text = userAboutInfo.first.orEmpty()
            tvUserProfileAccess.text = userAboutInfo.second.toString()
        }
    }

    private fun shareUserProfile() {
        val userPersonalInfo = mViewModel.showPersonalInfo()
        val userAboutInfo = mViewModel.showAboutInfo()
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hola te comparto mi perfil...\n" +
                    "${userPersonalInfo.first}\n" +
                    "${userPersonalInfo.second}\n" +
                    "${userPersonalInfo.third}\n" +
                    "${userAboutInfo.first}\n" +
                    "${userAboutInfo.second}\n")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun takeUserPhoto() {
        checkCameraHardware(mActivity)
    }

    private fun logOut(){
        val dialog = BaseDialog(
            mActivity,
            R.drawable.ic_ops_logout,
            getString(R.string.dialog_dealer_info_title),
            getString(R.string.dialog_dealer_info_message),
            getString(R.string.common_accept),
            yesAction = { mActivity.finish() }
        )
        dialog.show()
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}