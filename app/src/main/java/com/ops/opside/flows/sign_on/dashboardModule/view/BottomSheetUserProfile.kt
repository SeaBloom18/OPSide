package com.ops.opside.flows.sign_on.dashboardModule.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.utils.toast
import com.ops.opside.databinding.BottomSheetUserProfileBinding
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
    } //12 00 16 26 36 9
      //6601.90

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            //tvUserName.text = sharedPref?.getString(SP_NAME)
            ivBack.setOnClickListener { dismiss() }
            ivShareProfile.setOnClickListener { toast("Share Profile") }
            tvLogOut.setOnClickListener { closeSession() }
            ivChangePhoto.setOnClickListener {
                cameraPermission.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA))
                takeUserPhoto()
            }
        }


    }

    private fun takeUserPhoto() {
        checkCameraHardware(mActivity)
    }

    private fun closeSession() {
        //Close Session
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}