package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.content.Intent
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
    private val mDealerViewModel: DealerViewModel by viewModels()

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
        val userPersonalInfo = mDealerViewModel.showPersonalInfo()
        val userAboutInfo = mDealerViewModel.showAboutInfo()
        val userPricesInfo = mDealerViewModel.showPricesInfo()
        with(mBinding){
            tvConceUserName.text = userPersonalInfo.first
            tvConceUserEmail.text = userPersonalInfo.second
            tvConceUserPhone.text = userPersonalInfo.third
            tvConceAddress.text = userAboutInfo.first
            tvLineBusiness.text = userAboutInfo.second
            tvAbsenceAndMeters.text =
                "${userPricesInfo.second} ${getString(R.string.tv_prices_info_linear)}, " +
                        "${userPricesInfo.first} ${getString(R.string.tv_prices_info_absence)}"
            ivShareConceProfile.setOnClickListener { shareUserProfile() }
        }
    }

    /** Override and Methods **/
    private fun shareUserProfile() {
        val userPersonalInfo = mDealerViewModel.showPersonalInfo()
        val userAboutInfo = mDealerViewModel.showAboutInfo()
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hola te comparto mi perfil...\n" +
                    "${userPersonalInfo.first}\n" +
                    "${userPersonalInfo.second}\n" +
                    "${userPersonalInfo.third}\n" +
                    "${userAboutInfo.first}\n" +
                    "${userAboutInfo.second}\n")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
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