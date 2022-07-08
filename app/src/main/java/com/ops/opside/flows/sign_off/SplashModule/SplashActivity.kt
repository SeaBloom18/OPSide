package com.ops.opside.flows.sign_off.SplashModule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ops.opside.databinding.ActivitySplashBinding
import com.ops.opside.flows.sign_off.loginModule.view.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnLoginSplash.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}