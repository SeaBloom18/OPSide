package com.ops.opside.flows.sign_on.marketModule.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ops.opside.R
import com.ops.opside.databinding.ActivityMarketRegisterBinding

class MarketRegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMarketRegisterBinding
    private val concessionaires = arrayOf("David", "Mario", "Juan", "Luis")//<String>("David", "Mario", "Juan", "Luis")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMarketRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnViewConce.setOnClickListener { viewConcessionaries() }

        mBinding.ibSignInClose.setOnClickListener { finish() }

        mBinding.btnSelectLocation.setOnClickListener { startActivity(
            Intent(this, MarketLocationActivity::class.java)) }
    }

    private fun viewConcessionaries() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_concess, null)

        val autoCompUserName = view.findViewById<AutoCompleteTextView>(R.id.acUserName)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, concessionaires)
        autoCompUserName.setAdapter(adapter)


        dialog.setContentView(view)
        dialog.show()
    }
}