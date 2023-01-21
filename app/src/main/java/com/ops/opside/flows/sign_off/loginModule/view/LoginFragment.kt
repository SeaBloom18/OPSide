package com.ops.opside.flows.sign_off.loginModule.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.*
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.utils.*
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.databinding.FragmentLoginBinding
import com.ops.opside.flows.sign_off.loginModule.actions.LoginAction
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
import com.ops.opside.flows.sign_on.dealerModule.view.view.DealerActivity
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val mBinding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private val mViewModel: LoginViewModel by viewModels()
    private val mActivity: LoginActivity by lazy {
        activity as LoginActivity
    }

    private lateinit var mCollector: CollectorFE
    private lateinit var mConcessionaire: ConcessionaireFE
    private var mLinealMeterPrice: Double = 0.0
    private var mIsCollector: Boolean = true


    private val resultActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 100) {
                if (mBinding.teLoginEmail.text?.isEmpty() == true)
                    mBinding.teLoginEmail.setText(it.data?.getStringExtra("emailReturn").toString())
            }
        }

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
                logIn(
                    email = mBinding.teLoginEmail.text.toString().trim(),
                    password = mBinding.tePassword.text.toString().trim()
                )
            }

            tvSignUp.setOnClickListener {
                resultActivity.launch(Intent(mActivity, RegistrationActivity::class.java))
            }
        }

        bindViewModel()
        setFormWhitSharedPreferences()

        mViewModel.getLinealMeterPrice()
    }

    /**ViewModel SetUp**/
    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(mActivity, Observer(this::showLoading))
        mViewModel.getAction().observe(mActivity, Observer(this::handleAction))
        mViewModel.getLinealMetersPrice.observe(mActivity, Observer(this::getLinealMeterPrice))
    }


    private fun logIn(email: String, password: String) {
        if (email.isEmpty() && password.isEmpty()) {
            showErrorOnCredentials(getString(R.string.login_toast_empy_text))
            return
        }
        hideErrorOnCredentials()
        mBinding.tePassword.setText(password)
        mViewModel.searchForCollector(email)
    }

    private fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.InitSharedPreferences -> initSharedPreferences(action.useBiometrics)
            is LoginAction.LaunchHome -> launchHome()
            is LoginAction.TryLogIn -> logIn(action.user, action.pass)
            is LoginAction.DismissBtnBiometrics -> mBinding.btnBiometricsLogIn.isGone = true
            is LoginAction.SetUserData -> setUserData(action.collector, action.concessionaire)
            is LoginAction.ShowMessageError -> showError(action.error)
        }
    }

    private fun getLinealMeterPrice(price: Double) {
        mLinealMeterPrice = price
        mViewModel.checkBiometrics(this)
    }

    private fun setUserData(collector: CollectorFE?, concessionaire: ConcessionaireFE?) {
        if (collector != null) {
            mIsCollector = true
            mCollector = collector
            passwordUserValidation(mCollector.password, mBinding.tePassword.text.toString())
            return
        }

        if (concessionaire != null) {
            mIsCollector = false
            mConcessionaire = concessionaire
            passwordUserValidation(mConcessionaire.password, mBinding.tePassword.text.toString())
        }
    }

    private fun initSharedPreferences(useBiometrics: Boolean) {
        val result = if (mIsCollector) {
            mViewModel.initSPForCollector(
                mCollector,
                mLinealMeterPrice,
                mBinding.swRememberUser.isChecked,
                useBiometrics
            )
        } else {
            mViewModel.initSPForConcessionaire(
                mConcessionaire,
                mBinding.swRememberUser.isChecked,
                useBiometrics
            )
        }

        if (result.first) {
            mViewModel.getCurrentVersion()
        } else {
            showError(result.second)
        }
    }

    private fun setFinalizedForm() {
        mViewModel.updateRememberMe(mBinding.swRememberUser.isChecked)
        mBinding.tePassword.setText("")
        if (mViewModel.isRememberMe().not())
            mBinding.teLoginEmail.setText("")
    }

    private fun launchHome() {
        setFinalizedForm()
        when (if (mIsCollector) mCollector.role else (if (mConcessionaire.isForeigner)
            SP_FOREIGN_CONCE_ROLE else SP_NORMAL_CONCE_ROLE)) {
            SP_FOREIGN_CONCE_ROLE, SP_NORMAL_CONCE_ROLE -> {
                mActivity.startActivity<DealerActivity>()
            }

            SP_COLLECTOR_ROLE, SP_TAX_EXECUTOR_ROLE, SP_SUPER_USER_ROLE -> {
                if (mCollector.hasAccess || mCollector.role > SP_COLLECTOR_ROLE)
                    mActivity.startActivity<MainActivity>()
                else showError(getString(R.string.login_access_denied))
            }
        }
    }

    private fun setFormWhitSharedPreferences() {
        with(mBinding) {
            if (mViewModel.isBiometricsActivated()) {
                btnBiometricsLogIn.visibility = View.VISIBLE
                btnBiometricsLogIn.isClickable = true
            }

            if (mViewModel.isRememberMe()) {
                swRememberUser.isChecked = true
            }

            val loginPreferences = mViewModel.getLoginSp()
            if (loginPreferences.first.orEmpty().isNotEmpty()) {
                teLoginEmail.setText(loginPreferences.first)
            }
            if (loginPreferences.second.isNotEmpty()) {
                tvNameRemember.text =
                    getString(R.string.login_tv_remember_name, loginPreferences.second)
            }
        }
    }

    private fun passwordUserValidation(passwordServer: String, passwordLocal: String) {
        if (passwordServer != MD5.hashString(passwordLocal)) {
            showErrorOnCredentials(getString(R.string.login_toast_credentials_validation))
            return
        }
        hideErrorOnCredentials()
        checkIfSharedPreferencesISInit()
    }

    private fun checkIfSharedPreferencesISInit() {
        val emailRegistered = mViewModel.getLoginSp().first.orEmpty()
        val emailTyped =
            if (mIsCollector) {
                mCollector.email
            } else {
                mConcessionaire.email
            }

        if (mViewModel.isSPInitialized().not() || (emailTyped != emailRegistered)) {
            showBiometricsPermission()
        } else {
            mViewModel.getCurrentVersion()
        }
    }


    private fun showBiometricsPermission() {
        if (isAvailable(requireContext())) {
            val dialog = BaseDialog(
                imageResource = R.drawable.ic_ops_warning,
                context = mActivity,
                mTitle = getString(R.string.common_atention),
                mDescription = getString(R.string.login_biometrics_permission),
                buttonYesText = getString(R.string.common_accept),
                buttonNoText = getString(R.string.common_cancel),
                yesAction = {
                    mViewModel.saveCredentials(
                        credentials = "${
                            mBinding.teLoginEmail.text.toString().trim()
                        }:${mBinding.tePassword.text.toString().trim()}"
                    )
                },
                noAction = {
                    initSharedPreferences(useBiometrics = false)
                }
            )

            dialog.show()
        } else {
            initSharedPreferences(useBiometrics = false)
        }
    }

    private fun isAvailable(context: Context): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(context)
        return fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()
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
        val view = layoutInflater.inflate(R.layout.bottom_sheet_about_app, null)

        val tvTitle = view.findViewById<TextView>(R.id.tvBottomTitle)
        tvTitle.text = getString(R.string.ops_about_app_title)

        dialog.setContentView(view)
        dialog.show()
    }

}