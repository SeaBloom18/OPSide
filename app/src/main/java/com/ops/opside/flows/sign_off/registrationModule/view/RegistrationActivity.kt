package com.ops.opside.flows.sign_off.registrationModule.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
            btnRegister.setOnClickListener { bindViewModel() }
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

    private fun bindViewModel() {
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
        } else Toast.makeText(this, "llena todos los campos", Toast.LENGTH_SHORT).show()
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean{
        var isValid = true
        for (textField in textFields){
            if (textField.editText?.text.toString().trim().isEmpty()){
                textField.error = "Required"
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
            .setPositiveButton(getString(R.string.common_accept)) { dialogInterface, i ->
                when(checkedItem){
                    0 -> {
                        Toast.makeText(this, "Conce ", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        Toast.makeText(this, "conce fore", Toast.LENGTH_SHORT).show()
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
                //Toast.makeText(this, "Elegiste ${singleItems[which]}", Toast.LENGTH_SHORT).show()
            }
            .show()
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