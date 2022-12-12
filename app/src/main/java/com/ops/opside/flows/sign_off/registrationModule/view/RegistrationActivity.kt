package com.ops.opside.flows.sign_off.registrationModule.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.CollectorFE
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.OriginFE
import com.ops.opside.common.utils.MD5
import com.ops.opside.common.utils.clear
import com.ops.opside.common.utils.error
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityRegistrationBinding
import com.ops.opside.flows.sign_off.registrationModule.actions.RegistrationAction
import com.ops.opside.flows.sign_off.registrationModule.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class RegistrationActivity : BaseActivity() {

    private val mBinding: ActivityRegistrationBinding by lazy{
        ActivityRegistrationBinding.inflate(layoutInflater)
    }
    private val mViewModel: RegisterViewModel by viewModels()
    private val mConcessionaireFE: ConcessionaireFE = ConcessionaireFE()
    private val mCollectorFE: CollectorFE = CollectorFE()
    private var checkedItem = 0
    private var isValidPassword = false

    private lateinit var mOriginList: MutableList<OriginFE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.apply {
            btnRegister.setOnClickListener { insertUser() }

            tePassword.doAfterTextChanged { charSequence ->
                charSequence.toString().apply {
                    isValidPassword = isValidPassword()
                }
            }
            tvSeePolicies.setOnClickListener { showPolicies() }
        }

        bindViewModel()
        loadOriginList()
        setToolbar()
        alertDialogRegisterOptions()
        setupTextFields()
    }

    /** Toolbar MenuOptions and BackPressed**/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        bottomSheet()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.menu_user_register, menu)
        return true
    }

    private fun setToolbar(){
        with(mBinding.toolbarRegister.commonToolbar) {
            this.title = getString(R.string.login_txt_create_account_sign_in)
            setSupportActionBar(this)
            (context as RegistrationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

            this.addMenuProvider(object : MenuProvider{
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.regUser -> {
                            alertDialogRegisterOptions()
                            true
                        }
                        else -> false
                    }
                }
            })
        }
    }

    /** ViewModel Conf**/
    private fun bindViewModel(){
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))
        mViewModel.getAction().observe(this, Observer(this::handleAction))
        mViewModel.getOriginList.observe(this, Observer(this::getOriginList))
        mViewModel.getEmailExists.observe(this, Observer(this::getIsEmailExist))
    }

    private fun handleAction(action: RegistrationAction) {
        when(action) {
            is RegistrationAction.ShowMessageSuccess -> {
                bsRegisterSuccess()
                cleanEditText()
            }
            is RegistrationAction.ShowMessageError -> {
                bsRegisterError()
            }
        }
    }

    private fun getIsEmailExist(emailFS: Boolean) {
        isEmailExistValidationAndRegister(emailFS)
    }

    private fun loadOriginList(){
        mViewModel.getOriginList()
    }

    private fun getOriginList(originList: MutableList<OriginFE>){
        mOriginList = originList
        setUpOriginList()
    }

    /** OriginAutoCompleteText **/
    private fun setUpOriginList() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getOriginListNames())
        mBinding.teOrigin.setAdapter(adapter)
    }

    private fun getOriginListNames(): MutableList<String> {
        return mOriginList.map { it.originName }.toMutableList()
    }


    /** User FireStore Insert **/
    private fun insertUser(): Boolean{
        val isValid = false
        when(checkedItem){
            0, 1, 2 -> mViewModel.getIsEmailExist(mBinding.teEmail.text.toString().trim())
        }
        return isValid
    }

    private fun concessionaireViewModel(): Boolean {
        var isValid = false
        if(validateFields(mBinding.tilUserName, mBinding.tilLastName, mBinding.tilAddress,
                mBinding.tilPhone, mBinding.tilEmail, mBinding.tilPasswordConfirm, mBinding.tilOrigin)){
            if(!isValidEmail(mBinding.teEmail.text.toString().trim())){
                mBinding.tilEmail.error = getString(R.string.registration_toast_email_validation)
            } else {
                if (validatePassword()){
                    toast(getString(R.string.registration_toast_password_validation))
                } else {
                    if(isValidPassword){
                        if(mBinding.checkBoxPolicies.isChecked) {
                            with(mConcessionaireFE){
                                name = "${mBinding.teUserName.text.toString().trim()} ${mBinding.teLastName.text.toString().trim()}"
                                address = mBinding.teAddress.text.toString().trim()
                                phone = mBinding.tePhone.text.toString().trim()
                                email = mBinding.teEmail.text.toString().trim()
                                origin = mBinding.teOrigin.text.toString()
                                role = 2
                                password = MD5.hashString(mBinding.tePassword.text.toString().trim())
                                participatingMarkets = mutableListOf()
                                isForeigner = false
                                isValid = true
                            }
                        } else {
                            toast(getString(R.string.registration_toast_policies_validation))
                        }
                    } else {
                        toast(getString(R.string.registration_toast_password_regex_validation))
                    }
                }
            }
        } else toast(getString(R.string.registration_toast_fields_validation))
        return isValid
    }

    private fun foreignConcessionaireViewModel(): Boolean {
        var isValid = false
        if(validateFields(mBinding.tilUserName, mBinding.tilLastName, mBinding.tilEmail,
                mBinding.tilOrigin)){
            if(!isValidEmail(mBinding.teEmail.text.toString().trim())){
                mBinding.tilEmail.error = getString(R.string.registration_toast_email_validation)
            } else {
                if(mBinding.checkBoxPolicies.isChecked) {
                    with(mConcessionaireFE){
                        name = "${mBinding.teUserName.text.toString().trim()} ${mBinding.teLastName.text.toString().trim()}"
                        email = mBinding.teEmail.text.toString().trim()
                        origin = mBinding.teOrigin.text.toString()
                        isForeigner = true
                        role = 1
                        isValid = true
                    }
                } else {
                    toast(getString(R.string.registration_toast_password_regex_validation))
                }
            }
        } else toast(getString(R.string.registration_toast_fields_validation))
        return isValid
    }

    private fun collectorViewModel(): Boolean{
        var isValid = false
        if (validateFields(mBinding.tilUserName, mBinding.tilLastName, mBinding.tilAddress,
                mBinding.tilPhone, mBinding.tilEmail, mBinding.tilPasswordConfirm)){
            if(!isValidEmail(mBinding.teEmail.text.toString().trim())) {
                mBinding.tilEmail.error = getString(R.string.registration_toast_email_validation)
            } else {
                if (validatePassword()){
                    toast(getString(R.string.registration_toast_password_validation))
                } else {
                    if(isValidPassword) {
                        if(mBinding.checkBoxPolicies.isChecked) {
                            with(mCollectorFE){
                                name = "${mBinding.teUserName.text.toString().trim()} ${mBinding.teLastName.text.toString().trim()}"
                                address = mBinding.teAddress.text.toString().trim()
                                phone = mBinding.tePhone.text.toString().trim()
                                email = mBinding.teEmail.text.toString().trim()
                                password = MD5.hashString(mBinding.tePassword.text.toString().trim())
                                role = 3
                                isValid = true
                            }
                        } else {
                            toast(getString(R.string.registration_toast_policies_validation))
                        }
                    } else {
                        toast(getString(R.string.registration_toast_password_regex_validation))
                    }
                }
            }

        } else toast(getString(R.string.registration_toast_fields_validation))
        return isValid
    }

    /** Form Validations **/

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(): Boolean{
        var isValid = false
        if ((mBinding.tePassword.text.toString().trim()) != mBinding.tePasswordConfirm.text.toString().trim()){
            isValid = true
        }
        return isValid
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean{
        var isValid = true
        for (textField in textFields){
            if (textField.editText?.text.toString().trim().isEmpty()){
                textField.error = getString(R.string.login_til_required)
                isValid = false
            } else { textField.error = null }
        }
        return isValid
    }

    private fun CharSequence.isValidPassword(): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(this)
        return matcher.matches()
    }

    private fun setupTextFields() {
        with(mBinding){
            teUserName.addTextChangedListener { validateFields(tilUserName) }
            teLastName.addTextChangedListener { validateFields(tilLastName) }
            teAddress.addTextChangedListener { validateFields(tilAddress) }
            tePhone.addTextChangedListener { validateFields(tilPhone) }
            teEmail.addTextChangedListener { validateFields(tilEmail) }
        }
    }

    private fun isEmailExistValidationAndRegister(emailFS: Boolean){
        if (!isOnline(this)) toast("ops, it seems you have no connection")
        else {
            if (emailFS){
                mBinding.tilEmail.error = getString(R.string.registration_toast_password_exist_validation)
            } else {
                when(checkedItem){
                    0 -> if (concessionaireViewModel()) mViewModel.insertConcessionaire(mConcessionaireFE)
                    1 -> if (foreignConcessionaireViewModel()) mViewModel.insertForeignConcessionaire(mConcessionaireFE)
                    2 -> if (collectorViewModel()) mViewModel.insertCollector(mCollectorFE)
                }
            }
        }
    }

    /** Form Option and SetUp's **/
    private fun alertDialogRegisterOptions() {
        val singleItems = arrayOf(
            getString(R.string.registration_array_conce),
            getString(R.string.registration_array_foreign_conce),
            getString(R.string.registration_array_collector))

        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(getString(R.string.registration_alert_dialog_title))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.common_accept)) { _, _ ->
                when(checkedItem){
                    0 -> {
                        registerFormSetUP(checkedItem)
                        mBinding.tvFormTitle.text = getString(R.string.registration_tv_steps_title,
                            getString(R.string.registration_array_conce))
                    }
                    1 -> {
                        registerFormSetUP(checkedItem)
                        mBinding.tvFormTitle.text = getString(R.string.registration_tv_steps_title,
                            getString(R.string.registration_array_foreign_conce))
                    }
                    2 -> {
                        registerFormSetUP(checkedItem)
                        mBinding.tvFormTitle.text = getString(R.string.registration_tv_steps_title,
                            getString(R.string.registration_array_collector))
                    }
                }
            }
            .setNegativeButton(getString(R.string.registration_alert_dialog_negative_button)) { _, _ ->
                finish()
            }
            .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                checkedItem = which
            }
            .show()
    }

    private fun registerGeneralSetUp(){
        with(mBinding){
            tvFormTitle.visibility = View.VISIBLE
            teUserName.visibility = View.VISIBLE
            tilUserName.visibility = View.VISIBLE
            teLastName.visibility = View.VISIBLE
            tilLastName.visibility = View.VISIBLE
            teAddress.visibility = View.VISIBLE
            tilAddress.visibility = View.VISIBLE
            tePhone.visibility = View.VISIBLE
            tilPhone.visibility = View.VISIBLE
            teEmail.visibility = View.VISIBLE
            tilEmail.visibility = View.VISIBLE
            tePassword.visibility = View.VISIBLE
            tilPassword.visibility = View.VISIBLE
            tePasswordConfirm.visibility = View.VISIBLE
            tilPasswordConfirm.visibility = View.VISIBLE
            teOrigin.visibility = View.VISIBLE
            tilOrigin.visibility = View.VISIBLE
            checkBoxPolicies.visibility = View.VISIBLE

        }
    }

    private fun registerFormSetUP(formOption: Int){
        when (formOption) {
            0 -> { // Concessionaire
                registerGeneralSetUp()
            }
            1 -> { // Foreign Concessionaire
                registerGeneralSetUp()
                with(mBinding){
                    teAddress.visibility = View.GONE
                    tilAddress.visibility = View.GONE
                    tePhone.visibility = View.GONE
                    tilPhone.visibility = View.GONE
                    tilPassword.visibility = View.GONE
                    tePassword.visibility = View.GONE
                    checkBoxPolicies.visibility = View.VISIBLE
                }
            }
            2 -> { // Collector
                registerGeneralSetUp()
                with(mBinding){
                    teOrigin.visibility = View.GONE
                    tilOrigin.visibility = View.GONE
                }
            }
        }
    }

    private fun cleanEditText(){
        with(mBinding){
            teUserName.clear()
            tilUserName.error()

            teLastName.clear()
            tilLastName.error()

            teAddress.clear()
            tilAddress.error()

            tePhone.clear()
            tilPhone.error()

            teEmail.clear()
            tilEmail.error()

            tePassword.clear()
            tilPassword.error()

            tePasswordConfirm.clear()
            tilPasswordConfirm.error()
        }
    }

    /** BottomSheet **/
    private fun bsRegisterSuccess(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_success_registration, null)
        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        val anim = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        val tvBSTitle = view.findViewById<MaterialTextView>(R.id.tvBSRegistTitle)
        tvBSTitle.text = getString(R.string.registration_tv_success)
        anim.setAnimation(R.raw.success_lottie_anim)
        btnFinish.setOnClickListener { finish() }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun bsRegisterError(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_success_registration, null)
        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        val anim = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        val tvBSTitle = view.findViewById<MaterialTextView>(R.id.tvBSRegistTitle)
        tvBSTitle.text = getString(R.string.registration_tv_error)
        anim.setAnimation(R.raw.error_lottie_anim)
        btnFinish.setOnClickListener { finish() }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun bottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(R.string.registration_btn_bs_close)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.text = getString(R.string.registration_btn_exit)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showPolicies() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_global_info, null)

        val tvTitle = view.findViewById<TextView>(R.id.tvBottomTitle)
        tvTitle.setText(R.string.tv_title_policies)

        dialog.setContentView(view)
        dialog.show()
    }

    /** Internet Verify **/
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}