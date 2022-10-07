package com.ops.opside.flows.sign_off.loginModule.view

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.common.utils.*
import com.ops.opside.databinding.ActivityLoginBinding
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
import com.ops.opside.flows.sign_on.dealerModule.view.DealerActivity
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.EmailSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.zip.CRC32
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private val mViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            tvPolicies.setOnClickListener { showPolicies() }
            tvAboutApp.setOnClickListener { showAboutApp() }
            btnLogin.setOnClickListener {
                /*val email = mBinding.teLoginEmail.text.toString().trim()
                val password = mBinding.tePassword.text.toString().trim()
                if (email.isNotEmpty() && password.isNotEmpty()){
                    mViewModel.getUserLogin(mBinding.teLoginEmail.text.toString().trim())
                } else {
                    Toast.makeText(this@LoginActivity, R.string.login_toast_empy_text, Toast.LENGTH_SHORT).show()
                }*/

                if (preferences.getBoolean(SP_IS_INITIALIZED).not())
                    preferences.initPreferences(
                        12.5f,
                        "Mario Armando Razo Valenzuela",
                        "mario.v.r404@gmail.com",
                        "ajdfjadkfaks",
                        5,
                        true,
                        true
                    )

                launchActivity<MainActivity> {}
            }
            tvSignUp.setOnClickListener { launchActivity<RegistrationActivity> { } }
        }

        bindViewModel()
        setEmailSP()

        GlobalScope.launch{
           sendEmail()
        }
    }

    suspend fun sendEmail() {
        coroutineScope {
            launch(Dispatchers.IO) {
                EmailSender.send()
            }
        }
    }

    /**ViewModel SetUp**/
    private fun bindViewModel() {
        mViewModel.getUserLogin.observe(this, Observer(this::getPasswordUserValidation))
        mViewModel.getUserRole.observe(this, Observer(this::getUserRole))
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))
    }

    private fun setEmailSP() {
        val userPref = mViewModel.isRememberMeChecked()
        if (userPref.first) {
            mBinding.swRememberUser.isChecked = true
            mBinding.teLoginEmail.setText(userPref.second)
        }
    }

    private fun getPasswordUserValidation(password: String) {
        passwordUserValidation(password)
    }

    private fun getUserRole(userRole: String) {
        userRoleValidation(userRole)
    }

    private fun userRoleValidation(userRole: String) {
        Log.d("userRole", userRole)
        if (userRole == "1" || userRole == "2") {
            launchActivity<DealerActivity> { }
        } else {
            launchActivity<MainActivity> { }
        }
    }

    private fun passwordUserValidation(passwordFs: String) {
        var password = mBinding.tePassword.text.toString().trim()
        val crc32 = CRC32()
        crc32.update(password.toByteArray())
        password = String.format("%08X", crc32.value)
        if (passwordFs != password) {
            Toast.makeText(this, R.string.login_toast_credentials_validation, Toast.LENGTH_SHORT)
                .show()
        } else {
            if (mViewModel.isSPInitialized())
                mViewModel.initSP(
                    mBinding.teLoginEmail.text.toString().trim(),
                    mBinding.swRememberUser.isChecked
                )
        }
    }

    /**Override and other Methods**/
    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(Constants.BOTTOM_SHEET_BTN_CLOSE_APP)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.setText(Constants.BOTTOM_SHEET_TV_CLOSE_APP)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showPolicies() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_global_info, null)

        val tvTitle = view.findViewById<TextView>(R.id.tvBottomTitle)
        tvTitle.setText(com.firebase.ui.auth.R.string.fui_privacy_policy)
        val tvMessage = view.findViewById<TextView>(R.id.tvBottomLargeMessage)
        tvMessage.setText(com.firebase.ui.auth.R.string.fui_sms_terms_of_service_and_privacy_policy_extended)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showAboutApp() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_global_info, null)

        val tvTitle = view.findViewById<TextView>(R.id.tvBottomTitle)
        tvTitle.text = getString(R.string.login_tv_about_app)
        val tvMessage = view.findViewById<TextView>(R.id.tvBottomLargeMessage)
        tvMessage.setText(com.firebase.ui.auth.R.string.fui_sms_terms_of_service_and_privacy_policy_extended)

        dialog.setContentView(view)
        dialog.show()
    }
}