package com.ops.opside.flows.sign_on.dashboardModule.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        setUpPieChart()
    }

    private fun setUpPieChart() {


    }
}