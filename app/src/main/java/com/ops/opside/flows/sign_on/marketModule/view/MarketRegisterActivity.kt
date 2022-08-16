package com.ops.opside.flows.sign_on.marketModule.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.common.utils.Constants
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.databinding.ActivityMarketRegisterBinding

class MarketRegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMarketRegisterBinding
    private val concessionaires = arrayOf("David", "Mario", "Juan", "Luis")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMarketRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnViewConce.setOnClickListener { viewConcessionaire() }
            btnSelectLocation.setOnClickListener { launchActivity<MarketLocationActivity> {  } }
        }

        setToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(Constants.BOTTOM_SHEET_BTN_CLOSE_APP)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.setText(Constants.BOTTOM_SHEET_TV_CLOSE_APP)

        dialog.setContentView(view)
        dialog.show()
    }

    //Functions
    private fun setToolbar(){
        with(mBinding.toolbar.commonToolbar) {
            this.title = getString(R.string.registration_market_tv_title)
            setSupportActionBar(this)
            (context as MarketRegisterActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun viewConcessionaire() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_concess, null)

        val autoCompUserName = view.findViewById<AutoCompleteTextView>(R.id.acUserName)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, concessionaires)
        autoCompUserName.setAdapter(adapter)

        val group = view.findViewById<Group>(R.id.groupConce)

        val textViewList = view.findViewById<TextView>(R.id.tvViewAllConcess)
        textViewList.setOnClickListener {
            if (group.visibility == View.VISIBLE){ //Si la vista esta oculta
                //TransitionManager.beginDelayedTransition(holder.binding.marketCardView)
                group.visibility = View.GONE
                //holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_up)
            } else{ //Si la vista esta expuesta
                //TransitionManager.endTransitions(holder.binding.marketCardView)
                group.visibility = View.VISIBLE
                //holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_down)
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }
}