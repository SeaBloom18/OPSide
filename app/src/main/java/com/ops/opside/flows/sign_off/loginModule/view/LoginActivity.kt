package com.ops.opside.flows.sign_off.loginModule.view

import android.os.Bundle
import com.ops.opside.R
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val mBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, LoginFragment()).commit()
    }

    override fun onBackPressed() {
        val dialog = BottomSheetLoginBackPressed()
        dialog.show(this.supportFragmentManager, dialog.tag)
    }

}