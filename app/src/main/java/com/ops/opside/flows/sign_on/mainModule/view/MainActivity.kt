package com.ops.opside.flows.sign_on.mainModule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navView: BottomNavigationView = mBinding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_market, R.id.navigation_concessionaire)
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        val dialog = BaseDialog(
            this,
            R.drawable.ic_ops_logout,
            getString(R.string.dialog_dealer_info_title),
            getString(R.string.dialog_dealer_info_message),
            getString(R.string.common_accept),
            yesAction = { finish() }
        )
        dialog.show()
    }
}