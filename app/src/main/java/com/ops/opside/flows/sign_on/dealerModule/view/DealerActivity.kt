package com.ops.opside.flows.sign_on.dealerModule.view

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ops.opside.R
import com.ops.opside.databinding.ActivityDealerBinding

class DealerActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDealerBinding
    private lateinit var mDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDealerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnShowQr.setOnClickListener {
            mDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_show_qr, null)
            mDialog.setContentView(view)
            mDialog.show()

            view.findViewById<ImageButton>(R.id.imgBtnClose).setOnClickListener {
                mDialog.dismiss()
            }
        }


    }

}