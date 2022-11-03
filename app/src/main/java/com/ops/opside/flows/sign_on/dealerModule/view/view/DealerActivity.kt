package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.databinding.ActivityDealerBinding
import com.ops.opside.flows.sign_on.dealerModule.view.viewModel.DealerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DealerActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDealerBinding
    private val mViewModel: DealerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDealerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            ivShowQrCode.setOnClickListener { showQr() }
            tvLogOutConce.setOnClickListener { logOut() }
        }
        /** Calling Methods **/
        showPersonalInfo()
    }

    /** Override and Methods **/
    private fun showQr() {
        val dialog = BottomSheetShowQr()
        dialog.show(supportFragmentManager,dialog.tag)
    }

    private fun showPersonalInfo(){
        val userPersonalInfo = mViewModel.showPersonalInfo()
        val userAboutInfo = mViewModel.showAboutInfo()
        val userPricesInfo = mViewModel.showPricesInfo()
        with(mBinding){
            tvConceUserName.text = userPersonalInfo.first
            tvConceUserEmail.text = userPersonalInfo.second
            tvConceUserPhone.text = userPersonalInfo.third
            tvConceAddress.text = userAboutInfo.first
            tvLineBusiness.text = userAboutInfo.second
            tvAbsenceAndMeters.text =
                "${userPricesInfo.second} ${getString(R.string.tv_prices_info_linear)} " +
                        "${userPricesInfo.first} ${getString(R.string.tv_prices_info_absence)}"
        }
    }

    private fun logOut(){
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

    override fun onBackPressed() {
        logOut()
    }
}