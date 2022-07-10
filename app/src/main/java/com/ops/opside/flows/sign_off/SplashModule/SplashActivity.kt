package com.ops.opside.flows.sign_off.SplashModule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ops.opside.databinding.ActivitySplashBinding
import com.ops.opside.flows.sign_off.loginModule.view.LoginActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding
    private var i = 0
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        scope.launch {
            while (true){
                delay(2000)
                mBinding.progressBar.isIndeterminate = false
                mBinding.progressBar.isIndeterminate = true
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(4000L)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}
