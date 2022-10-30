package com.ops.opside.flows.sign_off.loginModule.view

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
//import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.BuildConfig
import com.ops.opside.R
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.common.utils.showLoading
import com.ops.opside.databinding.ActivityLoginBinding
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
import com.ops.opside.flows.sign_on.dealerModule.view.DealerActivity
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor
import java.util.zip.CRC32

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    /*private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo*/

    private lateinit var mBinding: ActivityLoginBinding
    private val mViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        /*executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()*/

        mBinding.apply {
            btnBiometricsLogIn.setOnClickListener { /*biometricPrompt.authenticate(promptInfo) */}
            tvPolicies.setOnClickListener { showPolicies() }
            tvAboutApp.setOnClickListener { showAboutApp() }
            btnLogin.setOnClickListener {

                //BuildVersion
                if (BuildConfig.DEBUG){
                    launchActivity<MainActivity> {  }
                } else {
                    val email = mBinding.teLoginEmail.text.toString().trim()
                    val password = mBinding.tePassword.text.toString().trim()
                    if (email.isNotEmpty() && password.isNotEmpty()){
                        hideError()
                        mViewModel.getUserLogin(mBinding.teLoginEmail.text.toString().trim())
                    } else {
                        showError(getString(R.string.login_toast_empy_text))
                    }
                }
            }
            tvSignUp.setOnClickListener { launchActivity<RegistrationActivity> {  } }
        }

        bindViewModel()
        setEmailSP()
    }

    /**ViewModel SetUp**/
    private fun bindViewModel(){
        mViewModel.getUserLogin.observe(this, Observer(this::getPasswordUserValidation))
        mViewModel.getUserRole.observe(this, Observer(this::getUserRole))
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))
    }

    private fun setEmailSP(){
        val userPref = mViewModel.isRememberMeChecked()
        with(mBinding){
            if (userPref.first){
                swRememberUser.isChecked = true
                mBinding.teLoginEmail.setText(userPref.second)
            }
        }

    }

    private fun getPasswordUserValidation(password: String){
        passwordUserValidation(password)
    }

    private fun getUserRole(userRole: String){
        userRoleValidation(userRole)
    }

    private fun userRoleValidation(userRole: String){
        Log.d("userRole", userRole)
        if (userRole == "1" || userRole == "2"){
            launchActivity<DealerActivity> {  }
        } else {
            launchActivity<MainActivity> {  }
        }
    }

    private fun passwordUserValidation(passwordFs: String){
        var password = mBinding.tePassword.text.toString().trim()
        val crc32 = CRC32()
        crc32.update(password.toByteArray())
        password = String.format("%08X", crc32.value)
        if (passwordFs != password){
            showError(getString(R.string.login_toast_credentials_validation))
        } else {
            hideError()
            mViewModel.initSP(mBinding.teLoginEmail.text.toString().trim(), mBinding.swRememberUser.isChecked)
            if (mViewModel.isSPInitialized())
                mViewModel.initSP(mBinding.teLoginEmail.text.toString().trim(), mBinding.swRememberUser.isChecked)
        }
    }

    /**Override and other Methods**/
    private fun showError(errorMessage: String){
        mBinding.tvError.tvError.isVisible = true
        mBinding.tvError.tvError.text = errorMessage

    }

    private fun hideError(){
        mBinding.tvError.tvError.isVisible = false
    }

    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(R.string.login_btn_close_bottom_sheet)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.setText(R.string.login_tv_title_bottom_sheet)

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