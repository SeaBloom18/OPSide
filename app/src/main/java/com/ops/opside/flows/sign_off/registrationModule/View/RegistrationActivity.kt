package com.ops.opside.flows.sign_off.registrationModule.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

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
}