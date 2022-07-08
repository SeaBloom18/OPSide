package com.ops.opside.flows.sign_on.dashboardModule.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ops.opside.R
import com.ops.opside.databinding.ActivityMainBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        supportActionBar?.hide()

        mBinding.fabInitTaxCollection.setOnClickListener {
            startActivity(Intent(this, TaxCollectionActivity::class.java))
        }
    }
}