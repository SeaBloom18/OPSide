package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ops.opside.BuildConfig
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityDealerBinding
import com.ops.opside.flows.sign_on.dealerModule.view.viewModel.DealerViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class DealerActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDealerBinding
    private val mDealerViewModel: DealerViewModel by viewModels()

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
                this.finish()
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    mBinding.ivProfilePictureConce.setImageURI(uri)
                    mBinding.lavUserProfileAnimConce.visibility = View.INVISIBLE
                    mBinding.btnSaveProfile.apply {
                        alpha = 1F
                        isEnabled = true
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDealerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            ivShowQrCode.setOnClickListener { showQr() }
            tvLogOutConce.setOnClickListener { logOut() }
            ivChangePhotoConce.setOnClickListener { takeImage() }
        }
        /** Calling Methods **/
        showPersonalInfo()
        bindViewModel()
    }

    /** ViewModel SetUp **/
    private fun bindViewModel() {
        mDealerViewModel.getShowProgress().observe(this, Observer(this::showLoading))
        mDealerViewModel.updateImage.observe(this, Observer(this::getImageUserUrl))
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

    /** Override and Methods **/
    private fun showQr() {
        val dialog = BottomSheetShowQr()
        dialog.show(supportFragmentManager,dialog.tag)
    }

    private fun showPersonalInfo(){
        val userPersonalInfo = mDealerViewModel.showPersonalInfo()
        val userAboutInfo = mDealerViewModel.showAboutInfo()
        val userPricesInfo = mDealerViewModel.showPricesInfo()
        with(mBinding){
            tvConceUserName.text = userPersonalInfo.first
            tvConceUserEmail.text = userPersonalInfo.second
            tvConceUserPhone.text = userPersonalInfo.third
            tvConceAddress.text = userAboutInfo.first
            tvLineBusiness.text = userAboutInfo.second
            tvAbsenceAndMeters.text =
                "${userPricesInfo.second} ${getString(R.string.tv_prices_info_linear)}, " +
                        "${userPricesInfo.first} ${getString(R.string.tv_prices_info_absence)}"
            ivShareConceProfile.setOnClickListener { shareUserProfile() }
        }
    }

    /** Override and Methods **/
    private fun shareUserProfile() {
        val userPersonalInfo = mDealerViewModel.showPersonalInfo()
        val userAboutInfo = mDealerViewModel.showAboutInfo()
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

    private fun logOut(){
        showDialog(
            imageResource = R.drawable.ic_ops_logout,
            title = getString(R.string.dialog_dealer_info_title),
            message = getString(R.string.dialog_dealer_info_message),
            buttonYesTitle = getString(R.string.common_accept),
            funButtonYes = { finish() }
        )

    }

    override fun onBackPressed() {
        logOut()
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
        val tmpFile = File.createTempFile("ops_profile_photo", ".png", this.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }
}