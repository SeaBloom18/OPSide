package com.ops.opside.flows.sign_off.loginModule.view

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.common.utils.Constants
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.databinding.ActivityLoginBinding
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            tvPolicies.setOnClickListener { showPolicies() }
            tvAboutApp.setOnClickListener { showAboutApp() }
            btnLogin.setOnClickListener { loginValidate() }
            tvSignUp.setOnClickListener { launchActivity<RegistrationActivity> {  } }
        }

        /*mBinding.btnLogin.setOnLongClickListener {
            startActivity(Intent(this, DealerActivity::class.java))
            return@setOnLongClickListener true
        }*/
    }

    //Methods
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
        tvTitle.text = "About App"
        val tvMessage = view.findViewById<TextView>(R.id.tvBottomLargeMessage)
        tvMessage.setText(com.firebase.ui.auth.R.string.fui_sms_terms_of_service_and_privacy_policy_extended)

        dialog.setContentView(view)
        dialog.show()    }

    private fun loginValidate(){
        val email = mBinding.teUserName.text.toString().trim()
        val password = mBinding.tePassword.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()){
            if (email == "admin@gmail.com" && password == ("12345")){
                launchActivity<MainActivity> {  }
            } else {
                Toast.makeText(this, R.string.login_toast_credentials_error, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.login_toast_empy_text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpViewModel() {
        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        /*mLoginViewModel.getUser()!!.observe(this){
            it.setEmail(mBinding.teUserName.text.toString().trim())
            it.setPassword(mBinding.tePassword.text.toString().trim())
        }*/
    }

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
}