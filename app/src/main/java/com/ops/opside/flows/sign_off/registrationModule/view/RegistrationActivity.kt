package com.ops.opside.flows.sign_off.registrationModule.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.Constants
import com.ops.opside.databinding.ActivityRegistrationBinding
import com.ops.opside.flows.sign_off.registrationModule.viewModel.RegisterViewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegistrationBinding
    private var dataBaseInstance = FirebaseFirestore.getInstance()

    private lateinit var mViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnRegister.setOnClickListener { bindViewModel() }
        }

        mViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        setToolbar()
        alertDialogRegisterOptions()
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
        mViewModel.insertConcessionaire(ConcessionaireSE(1, "David",
             "name", "address", "phone", "email", 0.0,
            "lineBusiness", 0, false, "password"))
    }

    private fun alertDialogRegisterOptions() {
        val singleItems = arrayOf("Concesionario", "Concesionario foraneo", "Recaudador")
        val checkedItem = -1

        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(getString(R.string.registration_alert_dialog_title))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.common_accept)) { dialogInterface, _ ->
                if (checkedItem >= 1) {
                    dialogInterface.dismiss()
                } else {
                    Toast.makeText(this, getString(R.string.registration_toast_option_validation),
                        Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.registration_alert_dialog_negative_button)) { _, _ ->
                finish()
            }
            .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                Toast.makeText(this, "Elegiste ${singleItems[which]}", Toast.LENGTH_SHORT).show()
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

    private fun concessionaireReregister(){
        /*if(){
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_success_registration, null)
            val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
            btnFinish.setOnClickListener { finish() }
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        } else {

        }*/

    }



    private fun bottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(Constants.BOTTOM_SHEET_BTN_CLOSE_REGIS)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.setText(Constants.BOTTOM_SHEET_TV_CLOSE_REGIS)

        dialog.setContentView(view)
        dialog.show()
    }
}