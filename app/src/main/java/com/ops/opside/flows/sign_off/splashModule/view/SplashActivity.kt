package com.ops.opside.flows.sign_off.splashModule.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ops.opside.common.utils.startActivity
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivitySplashBinding
import com.ops.opside.flows.sign_off.loginModule.view.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class SplashActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySplashBinding
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
            startActivity<LoginActivity>()
            finish()
        }
    }
}
