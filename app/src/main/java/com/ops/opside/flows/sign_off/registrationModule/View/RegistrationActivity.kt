package com.ops.opside.flows.sign_off.registrationModule.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ops.opside.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}