package com.ops.opside.flows.sign_off.loginModule.view

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.common.dialogs.InDevelopmentFragment
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.common.utils.showLoading
import com.ops.opside.databinding.ActivityLoginBinding
import com.ops.opside.databinding.FragmentLoginBinding
import com.ops.opside.flows.sign_off.loginModule.viewModel.LoginViewModel
import com.ops.opside.flows.sign_off.registrationModule.view.RegistrationActivity
import com.ops.opside.flows.sign_off.registrationModule.viewModel.RegisterViewModel
import com.ops.opside.flows.sign_on.dealerModule.view.view.DealerActivity
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.EmailSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.zip.CRC32

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val mBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, LoginFragment()).commit()
    }

    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(R.string.login_btn_close_bottom_sheet)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.setText(R.string.login_tv_title_bottom_sheet)

        dialog.setContentView(view)
        dialog.show()
    }

}