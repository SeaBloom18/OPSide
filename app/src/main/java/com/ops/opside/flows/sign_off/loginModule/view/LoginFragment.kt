package com.ops.opside.flows.sign_off.loginModule.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.utils.*
import com.ops.opside.databinding.FragmentLoginBinding
import com.ops.opside.flows.sign_off.loginModule.actions.LoginAction
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
import com.ops.opside.flows.sign_on.dealerModule.view.view.DealerActivity
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val mBinding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private val mViewModel: LoginViewModel by viewModels()
    private val mActivity: LoginActivity by lazy {
        activity as LoginActivity
    }

    private var userRol = 0
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {

            btnBiometricsLogIn.animateOnPress()
            btnBiometricsLogIn.setOnClickListener {
                mViewModel.checkBiometrics((this@LoginFragment) as Fragment)
            }

            tvPolicies.setOnClickListener { showPolicies() }
            tvAboutApp.setOnClickListener { showAboutApp() }

            btnLogin.setOnClickListener {
                //BuildVersion
                email = mBinding.teLoginEmail.text.toString().trim()
                password = mBinding.tePassword.text.toString().trim()
                logIn(email, password)
            }

            tvSignUp.setOnClickListener { mActivity.startActivity<RegistrationActivity>() }
        }

        bindViewModel()
        setFormWhitSharedPreferences()

        mViewModel.checkBiometrics(this)
    }

    private fun logIn(email: String, password: String) {
        this.email = email
        this.password = password

        if (email.isEmpty() && password.isEmpty()) {
            showErrorOnCredentials(getString(R.string.login_toast_empy_text))
            return
        }

        mViewModel.getUserLogin(email)
        hideErrorOnCredentials()
    }


    /**ViewModel SetUp**/
    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(mActivity, Observer(this::showLoading))
        mViewModel.getAction().observe(mActivity, Observer(this::handleAction))
        mViewModel.getUserLogin.observe(mActivity, Observer(this::passwordUserValidation))
        mViewModel.getUserRole.observe(mActivity, Observer(this::userRoleValidation))
    }

    private fun showLoading(show: Boolean) {
        mActivity.showLoading(show)
    }

    private fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.InitSharedPreferences -> initSharedPreferences(action.useBiometrics)
            is LoginAction.LaunchHome -> launchHome()
            is LoginAction.TryLogIn -> logIn(action.user, action.pass)
            is LoginAction.DismissBtnBiometrics -> mBinding.btnBiometricsLogIn.isGone = true
        }
    }

    private fun initSharedPreferences(useBiometrics: Boolean) {
        mViewModel.initSP(
            email,
            mBinding.swRememberUser.isChecked,
            useBiometrics
        )
    }

    private fun launchHome() {
        when(userRol){
            1,2 -> mActivity.startActivity<DealerActivity>()
            3,4,5 -> mActivity.startActivity<MainActivity>()
        }
    }

    private fun setFormWhitSharedPreferences() {
        val userPref = mViewModel.isRememberMeChecked()
        with(mBinding) {
            if (userPref.first) {
                swRememberUser.isChecked = true
                teLoginEmail.setText(userPref.second)
                tvNameRemember.text =
                    getString(
                        R.string.login_tv_remember_name,
                        userPref.third.orEmpty().split(" ")[0]
                    )
            } else {
                tvNameRemember.text = getString(R.string.login_welcome)
            }
        }
    }

    private fun userRoleValidation(userRole: String) {
        userRol = userRole.toInt()
        launchHome()
    }

    private fun passwordUserValidation(passwordFs: String) {
        var password = this.password

        password = MD5.hashString(password)
        if (passwordFs != password) {
            showErrorOnCredentials(getString(R.string.login_toast_credentials_validation))
            return
        }

        hideErrorOnCredentials()
        checkIfSharedPreferencesISInit()
    }

    private fun checkIfSharedPreferencesISInit(){
        if (mViewModel.isSPInitialized()) {
            showBiometricsPermission()
            return
        }

        userRoleValidation(mViewModel.getRol().toString())
    }

    private fun showBiometricsPermission() {
        val dialog = BaseDialog(
            imageResource = R.drawable.ic_ops_warning,
            context = mActivity,
            mTitle = getString(R.string.common_atention),
            mDescription = "¿Deseas usar tus datos biometricos para acceder al sistema?",
            buttonYesText = getString(R.string.common_accept),
            buttonNoText = getString(R.string.common_cancel),
            yesAction = {
                mViewModel.saveCredentials(
                    credentials = "$email:$password"
                )
            },
            noAction = {
                initSharedPreferences(useBiometrics = false)
            }
        )

        dialog.show()
    }

    private fun showErrorOnCredentials(errorMessage: String) {
        mBinding.tvError.tvError.isVisible = true
        mBinding.tvError.tvError.text = errorMessage
    }

    private fun hideErrorOnCredentials() {
        mBinding.tvError.tvError.isVisible = false
    }

    @SuppressLint("InflateParams")
    private fun showPolicies() {
        val dialog = BottomSheetDialog(mActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_global_info, null)

        val tvTitle = view.findViewById<TextView>(R.id.tvBottomTitle)
        tvTitle.setText(R.string.tv_title_policies)

        dialog.setContentView(view)
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showAboutApp() {
        val dialog = BottomSheetDialog(mActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_global_info, null)

        val tvTitle = view.findViewById<TextView>(R.id.tvBottomTitle)
        tvTitle.text = getString(R.string.login_tv_about_app)

        dialog.setContentView(view)
        dialog.show()
    }

}