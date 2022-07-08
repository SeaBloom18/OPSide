package com.ops.opside.flows.sign_off.loginModule.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ops.opside.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}