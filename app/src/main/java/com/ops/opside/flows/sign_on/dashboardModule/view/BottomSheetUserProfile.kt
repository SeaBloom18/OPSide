package com.ops.opside.flows.sign_on.dashboardModule.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.utils.toast
import com.ops.opside.databinding.BottomSheetUserProfileBinding
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.BottomSheetUserProfileViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

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

    private val cameraPermission = registerForActivityResult(ActivityResultContracts
        .RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
                // Only approximate location access granted.
            } else -> {
            mActivity.finish()
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
        mBinding.apply {
            //tvUserName.text = sharedPref?.getString(SP_NAME)
            ivBack.setOnClickListener { dismiss() }
            tvLogOut.setOnClickListener { logOut() }
            ivChangePhoto.setOnClickListener {
                cameraPermission.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA))
                takeUserPhoto()
            }
            ivShareProfile.setOnClickListener { shareUserProfile() }
        }

        /** Methods Calls **/
        showPersonalUserInfo()
    }

    /** Other Methods**/
    private fun showPersonalUserInfo(){
        val userPersonalInfo = mViewModel.showPersonalInfo()
        val userAboutInfo = mViewModel.showAboutInfo()
        with(mBinding){
            tvUserName.text = userPersonalInfo.first
            tvUserEmail.text = userPersonalInfo.second
            tvUserPhone.text = userPersonalInfo.third

            tvUserProfileOrigin.text = userAboutInfo.first
            tvUserProfileType.text = userAboutInfo.second
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