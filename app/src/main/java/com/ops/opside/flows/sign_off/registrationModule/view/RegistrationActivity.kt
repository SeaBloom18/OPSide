package com.ops.opside.flows.sign_off.registrationModule.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.R
import com.ops.opside.common.utils.Constants
import com.ops.opside.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegistrationBinding
    private var dataBaseInstance = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setToolbar()
        alertDialogRegisterOptions()
        formSetUp()
        concessionaireRegister()

        mBinding.btnRegister.setOnClickListener {
            //if()
            showBottomSheetSuccess()
        }
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

    //Methods
    private fun concessionaireRegister() {
        //Validate textInputLayout
        val concessionaire: MutableMap<String, Any> = HashMap()
        /*concessionaire["name"] = mBinding.teUserName.text.toString().trim()
        concessionaire["lastName"] = mBinding.teLastName.text.toString().trim()
        concessionaire["address"] = mBinding.teAddress.text.toString().trim()
        concessionaire["phone"] = mBinding.tePhone.text.toString().trim()
        concessionaire["email"] = mBinding.teEmail.text.toString().trim()
        concessionaire["password"] = mBinding.tePassword.text.toString().trim()*/

        concessionaire["name"] = "Mario"
        concessionaire["lastName"] = "Gonzalez"
        concessionaire["address"] = "Jardines de babilonia #31"
        concessionaire["phone"] = "3328411633"
        concessionaire["email"] = "dagq117@gmail.com"
        concessionaire["password"] = "12345"

        dataBaseInstance.collection("concessionaires")
            .add(concessionaire)
            .addOnSuccessListener { documentReference ->
                Log.d("Firebase", "DocumentSnapshot added with ID: " + documentReference.id)
            }
            .addOnFailureListener {
                    e -> Log.w("Firebase", "Error adding document", e)
            }
    }

    private fun formSetUp(){
        mBinding.teUserName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mBinding.tilLastName.visibility = View.VISIBLE
                mBinding.teLastName.visibility = View.VISIBLE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val userName = mBinding.teUserName.text.toString().trim()
                if (userName.length <= 10){
                    mBinding.teLastName.visibility = View.INVISIBLE
                    mBinding.tilLastName.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        mBinding.teLastName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mBinding.tilAddress.visibility = View.VISIBLE
                mBinding.teAddress.visibility = View.VISIBLE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val lastName = mBinding.teLastName.text.toString().trim()
                if (lastName.length <= 10){
                    mBinding.teAddress.visibility = View.INVISIBLE
                    mBinding.tilAddress.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        mBinding.teAddress.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mBinding.tilPhone.visibility = View.VISIBLE
                mBinding.tePhone.visibility = View.VISIBLE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val address = mBinding.teAddress.text.toString().trim()
                if (address.length <= 10){
                    mBinding.tePhone.visibility = View.INVISIBLE
                    mBinding.tilPhone.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        mBinding.tePhone.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mBinding.tilEmail.visibility = View.VISIBLE
                mBinding.teEmail.visibility = View.VISIBLE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val phone = mBinding.tePhone.text.toString().trim()
                if (phone.length <= 9){
                    mBinding.teEmail.visibility = View.INVISIBLE
                    mBinding.tilEmail.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        mBinding.teEmail.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mBinding.tilPassword.visibility = View.VISIBLE
                mBinding.tePassword.visibility = View.VISIBLE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val email = mBinding.tePhone.text.toString().trim()
                if (email.length <= 10){
                    mBinding.tePassword.visibility = View.INVISIBLE
                    mBinding.tilPassword.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        mBinding.tePassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mBinding.tilPasswordConfirm.visibility = View.VISIBLE
                mBinding.tePasswordConfirm.visibility = View.VISIBLE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val email = mBinding.tePassword.text.toString().trim()
                if (email.length <= 10){
                    mBinding.tePasswordConfirm.visibility = View.INVISIBLE
                    mBinding.tilPasswordConfirm.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
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

    private fun showBottomSheetSuccess(){
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
        tvTitle.setText(Constants.BOTTOM_SHEET_TV_CLOSE_REGIS)

        dialog.setContentView(view)
        dialog.show()
    }

    data class conceTest(
        var conceId: String? = null,
        var conceName: String? = null,
        var conceLastName: String? = null,
    )
}