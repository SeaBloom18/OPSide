package com.ops.opside.flows.sign_off.loginModule.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.utils.Constants
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.databinding.ActivityLoginBinding
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private val mViewModel: LoginViewModel by viewModels()
    private lateinit var mFirebaseUser: MutableList<String>

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

       /* var password = ""
        firestore.collection(Constants.FIRESTORE_CONCESSIONAIRES)
            .whereEqualTo("email", "dagq117@gmail.com")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    password = document.data["password"].toString()
                    Log.d("loginFirestore", "${document.id} => ${document.data["password"]}")
                    Toast.makeText(this, password, Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                Log.w("loginFirestore", "Error getting documents: ", exception)
            }*/

        /*if (mViewModel.isSPInitialized()){
        }*/
        /*mViewModel.getUserLogin()
        Toast.makeText(this, mViewModel.getUserLogin(), Toast.LENGTH_SHORT).show()
        Log.d("LoginFirebase", mViewModel.getUserLogin())*/

        /*Toast.makeText(this, "${bindViewModel()}", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "${getFirebaseUser()}", Toast.LENGTH_SHORT).show()*/

        //Toast.makeText(this, "${mViewModel.getUserLogin()}", Toast.LENGTH_SHORT).show()
        bindViewModel()
        getFirebaseUser()
    }

    //Methods
    private fun bindViewModel(){
        mViewModel.getUserLogin.observe(this, Observer(this::getFirebaseUser))
    }

    private fun getFirebaseUser(fireBaseUser: MutableList<String>){
        mFirebaseUser = fireBaseUser
        Log.d("passwordView", mFirebaseUser.toString())
    }

    private fun getFirebaseUser(){
        mViewModel.getUserLogin()
        Log.d("passwordView", mViewModel.getUserLogin().toString())
    }

    /*fun getUserByEmail(): String {
        var password = ""
        firestore.collection(Constants.FIRESTORE_CONCESSIONAIRES)
            .whereEqualTo("email", "dagq117@gmail.com")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    password = document.data["password"].toString()
                    Log.d("loginFirestore", "${document.id} => ${document.data["password"]}")
                    Toast.makeText(this, password, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("loginFirestore", "Error getting documents: ", exception)
            }
        return password
    }*/

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
        dialog.show()
    }

    private fun loginValidate(){
        /*val email = mBinding.teUserName.text.toString().trim()
        val password = mBinding.tePassword.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()){
            if (email == "admin@gmail.com" && password == ("12345")){
                launchActivity<MainActivity> {  }
            } else {
                Toast.makeText(this, R.string.login_toast_credentials_error, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.login_toast_empy_text, Toast.LENGTH_SHORT).show()
        }*/

       /* if (mViewModel.isSPInitialized()) {
            mViewModel.initSP()
        }*/

        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun setUpViewModel() {
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