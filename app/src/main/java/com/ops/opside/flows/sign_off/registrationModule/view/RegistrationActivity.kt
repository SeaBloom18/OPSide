package com.ops.opside.flows.sign_off.registrationModule.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.common.Utils.Constants
import com.ops.opside.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setToolbar()

        mBinding.btnRegister.setOnClickListener { showBottomSheetSuccess() }

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
}