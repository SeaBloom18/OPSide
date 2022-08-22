package com.ops.opside.flows.sign_off.registrationModule.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.Constants
import com.ops.opside.databinding.ActivityRegistrationBinding
import com.ops.opside.flows.sign_off.registrationModule.viewModel.RegisterViewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegistrationBinding
    private lateinit var mViewModel: RegisterViewModel
    private lateinit var mConcessionaireSE: ConcessionaireSE
    private var checkedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnRegister.setOnClickListener { insertUser() }
        }

        mViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        mConcessionaireSE = ConcessionaireSE()

        setToolbar()
        alertDialogRegisterOptions()
        setupTextFields()
    }

    //Override Methods
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        bottomSheet()
    }

    private fun insertUser(){
        when(checkedItem){
            0 -> {
                concessionaireViewModel()
            }
            1 -> {
                foreignConcessionaireViewModel()
            }
            2 -> {

            }
        }
    }

    private fun concessionaireViewModel() {
        if(validateFields(
                mBinding.tilUserName,
                mBinding.tilLastName,
                mBinding.tilAddress,
                mBinding.tilPhone,
                mBinding.tilEmail,
                mBinding.tilPassword)){
            with(mConcessionaireSE){
                name = "${mBinding.teUserName.text.toString().trim()} ${mBinding.teLastName.text.toString().trim()}"
                address = mBinding.teAddress.text.toString().trim()
                phone = mBinding.tePhone.text.toString().trim()
                email = mBinding.teEmail.text.toString().trim()
                password = mBinding.tePassword.text.toString().trim()

                mViewModel.insertConcessionaire(mConcessionaireSE)
                bsRegisterSuccess()
            }
        } else Toast.makeText(this, getString(R.string.registration_toast_fields_validation),
            Toast.LENGTH_SHORT).show()
    }

    private fun foreignConcessionaireViewModel() {
        if(validateFields(
                mBinding.tilUserName,
                mBinding.tilLastName,
                mBinding.tilEmail,
                mBinding.tilPassword)){
            with(mConcessionaireSE){
                name = "${mBinding.teUserName.text.toString().trim()} ${mBinding.teLastName.text.toString().trim()}"
                email = mBinding.teEmail.text.toString().trim()
                password = mBinding.tePassword.text.toString().trim()

                mViewModel.insertForeignConcessionaire(mConcessionaireSE)
                bsRegisterSuccess()
            }
        } else Toast.makeText(this, getString(R.string.registration_toast_fields_validation),
            Toast.LENGTH_SHORT).show()
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

    private fun setupTextFields() {
        with(mBinding){
            teUserName.addTextChangedListener { validateFields(tilUserName) }
            teLastName.addTextChangedListener { validateFields(tilLastName) }
            teAddress.addTextChangedListener { validateFields(tilAddress) }
            tePhone.addTextChangedListener { validateFields(tilPhone) }
            teEmail.addTextChangedListener { validateFields(tilEmail) }
            tePassword.addTextChangedListener { validateFields(tilPassword) }
        }
    }

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
                        Toast.makeText(this, "Conce ", Toast.LENGTH_SHORT).show()
                        setUpConcessionaire()
                    }
                    1 -> {
                        Toast.makeText(this, "conce fore", Toast.LENGTH_SHORT).show()
                        setUpForeignConcessionaire()
                    }
                    2 -> {
                        Toast.makeText(this, " collector", Toast.LENGTH_SHORT).show()
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

    private fun setUpForeignConcessionaire() {
        mBinding.tvFormTitle.visibility = View.VISIBLE

        mBinding.teUserName.visibility = View.VISIBLE
        mBinding.tilUserName.visibility = View.VISIBLE

        mBinding.teLastName.visibility = View.VISIBLE
        mBinding.tilLastName.visibility = View.VISIBLE

        mBinding.teAddress.visibility = View.GONE
        mBinding.tilAddress.visibility = View.GONE

        mBinding.tePhone.visibility = View.GONE
        mBinding.tilPhone.visibility = View.GONE

        mBinding.teEmail.visibility = View.VISIBLE
        mBinding.tilEmail.visibility = View.VISIBLE

        mBinding.tePassword.visibility = View.VISIBLE
        mBinding.tilPassword.visibility = View.VISIBLE

        mBinding.tePasswordConfirm.visibility = View.VISIBLE
        mBinding.tilPasswordConfirm.visibility = View.VISIBLE
    }

    private fun setUpConcessionaire(){
        mBinding.tvFormTitle.visibility = View.VISIBLE

        mBinding.teUserName.visibility = View.VISIBLE
        mBinding.tilUserName.visibility = View.VISIBLE

        mBinding.teLastName.visibility = View.VISIBLE
        mBinding.tilLastName.visibility = View.VISIBLE

        mBinding.teAddress.visibility = View.VISIBLE
        mBinding.tilAddress.visibility = View.VISIBLE

        mBinding.tePhone.visibility = View.VISIBLE
        mBinding.tilPhone.visibility = View.VISIBLE

        mBinding.teEmail.visibility = View.VISIBLE
        mBinding.tilEmail.visibility = View.VISIBLE

        mBinding.tePassword.visibility = View.VISIBLE
        mBinding.tilPassword.visibility = View.VISIBLE

        mBinding.tePasswordConfirm.visibility = View.VISIBLE
        mBinding.tilPasswordConfirm.visibility = View.VISIBLE

    }

    private fun setToolbar(){
        with(mBinding.toolbarRegister.commonToolbar) {
            this.title = getString(R.string.login_txt_create_account_sign_in)
            setSupportActionBar(this)
            (context as RegistrationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun bsRegisterSuccess(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_success_registration, null)
        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setOnClickListener { finish() }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun bottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(Constants.BOTTOM_SHEET_BTN_CLOSE_REGIS)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.text = getString(R.string.registration_btn_exit)

        dialog.setContentView(view)
        dialog.show()
    }
}