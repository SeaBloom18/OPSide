package com.ops.opside.flows.sign_on.dashboardModule.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.ops.opside.BuildConfig
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetUserProfileBinding
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.BottomSheetUserProfileViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 28/10/22.
 */

@AndroidEntryPoint
class BottomSheetUserProfile : BaseBottomSheetFragment() {

    @Inject
    lateinit var preferences: Preferences

    private val mBinding: BottomSheetUserProfileBinding by lazy {
        BottomSheetUserProfileBinding.inflate(layoutInflater)
    }

    private val mViewModel: BottomSheetUserProfileViewModel by viewModels()
    private val mActivity: MainActivity by lazy { activity as MainActivity }

    /** Camera setUp **/
    private var latestTmpUri: Uri? = null

    private val cameraPermission = registerForActivityResult(
        ActivityResultContracts
            .RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
            }
            permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
            }
            else -> {
                mActivity.finish()
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    mBinding.ivProfilePicture.setImageURI(uri)
                    mBinding.lavUserProfileAnim.visibility = View.INVISIBLE
                    mBinding.btnSaveProfile.apply {
                        alpha = 1F
                        isEnabled = true
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA
            )
        )
        takeUserPhoto()
        mBinding.apply {
            ivBack.setOnClickListener { dismiss() }
            tvLogOut.setOnClickListener { logOut() }
            ivChangePhoto.setOnClickListener { takeImage() }
            ivShareProfile.setOnClickListener { shareUserProfile() }
            btnSaveProfile.setOnClickListener {
                latestTmpUri?.let { it1 ->
                    mViewModel.uploadUserImage(it1)
                }
            }
        }

        /** Methods Calls **/
        showPersonalUserInfo()
        bindViewModel()
    }

    /** ViewModel SetUp **/
    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(mActivity, Observer(this::showLoading))
        mViewModel.updateImage.observe(mActivity, Observer(this::getImageUserUrl))
    }

    private fun getImageUserUrl(imageUserUrlSuccess: Boolean) {
        Log.d("imageUrl", imageUserUrlSuccess.toString())
        if (imageUserUrlSuccess){
            toast(getString(R.string.bottom_sheet_image_upload_success))
            mBinding.btnSaveProfile.apply {
                isEnabled = false
                alpha = 0.5F
            }
        } else toast(getString(R.string.bottom_sheet_image_upload_error))
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
        val tmpFile = File.createTempFile("ops_profile_photo", ".png", mActivity.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            mActivity,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun takeUserPhoto() {
        checkCameraHardware(mActivity)
    }

    /** Other Methods**/
    private fun showPersonalUserInfo() {
        val userPersonalInfo = mViewModel.showPersonalInfo()
        val userAboutInfo = mViewModel.showAboutInfo()
        with(mBinding) {
            tvUserName.text = userPersonalInfo.first
            tvUserEmail.text = userPersonalInfo.second
            tvUserPhone.text = userPersonalInfo.third

            tvUserProfileAdress.text = userAboutInfo.first.orEmpty()
            if (userAboutInfo.second.toString().isNotEmpty()) {
                mBinding.ivProfilePicture.visibility = View.VISIBLE
                mBinding.lavUserProfileAnim.visibility = View.INVISIBLE
                Glide.with(mActivity)
                    .load(userAboutInfo.second).circleCrop().into(mBinding.ivProfilePicture)
            }
        }
    }

    private fun shareUserProfile() {
        val userPersonalInfo = mViewModel.showPersonalInfo()
        val userAboutInfo = mViewModel.showAboutInfo()
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, getString(R.string.bottom_sheet_share_profile_title) + "\n" +
                        "${userPersonalInfo.first}\n" +
                        "${userPersonalInfo.second}\n" +
                        "${userPersonalInfo.third}\n" +
                        "${userAboutInfo.first}\n" +
                        "${userAboutInfo.second}\n"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun logOut() {
        val dialog = BaseDialog(
            mActivity,
            R.drawable.ic_ops_logout,
            getString(R.string.dialog_dealer_info_title),
            getString(R.string.dialog_dealer_info_message),
            getString(R.string.common_accept),
            yesAction = {
                preferences.deletePreferences()
                mActivity.finish()
            }
        )
        dialog.show()
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}