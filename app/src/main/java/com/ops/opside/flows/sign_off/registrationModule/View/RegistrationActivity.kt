package com.ops.opside.flows.sign_off.registrationModule.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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

        mBinding.imgBtnClose.setOnClickListener { bottomSheet() }

        mBinding.btnRegister.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_success_registration, null)
            val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
            btnFinish.setOnClickListener { finish() }
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    override fun onBackPressed() {
        bottomSheet()
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