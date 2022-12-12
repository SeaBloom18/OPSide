package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ops.opside.BuildConfig
import com.ops.opside.R
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityDealerBinding
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
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

        cameraPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA
            )
        )

        takeUserPhoto()

        mBinding.apply {
            ivChangePhotoConce.setOnClickListener { takeImage() }
            btnSaveProfile.setOnClickListener {
                latestTmpUri?.let { it1 ->
                    mDealerViewModel.uploadUserImage(it1)
                }
            }
        }
        /** Calling Methods **/
        setToolbar()
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
            //ivShareConceProfile.setOnClickListener { shareUserProfile() }
            Log.d("userURLPhoto", userAboutInfo.third.toString())
            if (userAboutInfo.third.toString().isNotEmpty()) {
                mBinding.ivProfilePictureConce.visibility = View.VISIBLE
                mBinding.lavUserProfileAnimConce.visibility = View.INVISIBLE
                Glide.with(this@DealerActivity)
                    .load(userAboutInfo.third).circleCrop().into(mBinding.ivProfilePictureConce)
            }

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

    private fun takeUserPhoto() {
        checkCameraHardware(this)
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    /** Toolbar MenuOptions and BackPressed**/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dealer_activity, menu)
        return true
    }

    private fun setToolbar(){
        with(mBinding.toolbarDealer.commonToolbar) {
            this.title = getString(R.string.tv_dealer_title)
            setSupportActionBar(this)
            (context as DealerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.showQR -> {
                            showQr()
                            true
                        }
                        R.id.shareProfile -> {
                            shareUserProfile()
                            true
                        }
                        else -> false
                    }
                }
            })
        }
    }
}