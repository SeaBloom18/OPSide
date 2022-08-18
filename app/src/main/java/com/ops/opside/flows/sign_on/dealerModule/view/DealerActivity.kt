package com.ops.opside.flows.sign_on.dealerModule.view

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ops.opside.R
import com.ops.opside.databinding.ActivityDealerBinding

class DealerActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDealerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDealerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnShowQr.setOnClickListener {
                showQr()
            }
        }

    }

    private fun showQr() {
        val dialog = BottomSheetShowQr()
        dialog.show(supportFragmentManager,dialog.tag)
    }

}