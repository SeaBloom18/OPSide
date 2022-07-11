package com.ops.opside.flows.sign_off.loginModule.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.databinding.ActivityLoginBinding
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.View.RegistrationActivity
import com.ops.opside.flows.sign_on.dashboard.MainActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        mBinding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        //setUpViewModel()
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
        val view = layoutInflater.inflate(R.layout.bottom_sheet_close_app, null)
        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setOnClickListener { finish() }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }
}