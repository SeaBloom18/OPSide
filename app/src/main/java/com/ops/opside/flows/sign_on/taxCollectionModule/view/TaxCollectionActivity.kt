package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.utils.launchFragment
import com.ops.opside.databinding.ActivityTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.interfaces.TaxCollectionAux

class TaxCollectionActivity : AppCompatActivity(), TaxCollectionAux {

    private lateinit var mBinding: ActivityTaxCollectionBinding
    private lateinit var mSelectedTianguis: TianguisSE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTaxCollectionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {

            fabRecord.animateOnPress()
            fabRecord.setOnClickListener {
                bsdRecordTaxCollection()
            }

            btnFinalize.setOnClickListener {
                showAlertFinalize()
            }
        }

        setUpPieChart()
        bsdPickTianguis()
    }

    private fun showAlertFinalize() {
        val dialog = BaseDialog(
            this@TaxCollectionActivity,
            getString(R.string.common_atention),
            getString(R.string.tax_collection_finalize_collection),
            getString(R.string.common_accept),
            "",
            { launchFinalizeFragment() }
        )
        dialog.show()
    }

    private fun launchFinalizeFragment() {
        val bundle = Bundle()
        bundle.putString("type", "create")

        launchFragment(
            FinalizeTaxCollectionFragment(),
            supportFragmentManager,
            R.id.containerTaxCollection,
            bundle
        )
    }

    private fun bsdPickTianguis() {
        val dialog = BottomSheetPickTianguis {
            mSelectedTianguis = it
            Log.d("Demo", mSelectedTianguis.toString())
        }

        dialog.isCancelable = false
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun setUpPieChart() {
        mBinding.chartTaxMoney.apply {
            progress = 65f
            setProgressWithAnimation(65f, 4000) // =1s

            // Set Progress Max
            progressMax = 200f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = getColor(R.color.secondaryColor)
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = getColor(R.color.primaryLightColor)
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 28f // in DP
            backgroundProgressBarWidth = 30f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }


    private fun bsdRecordTaxCollection() {
        val dialog = BottomSheetRecordTaxCollection()
        dialog.show(supportFragmentManager, dialog.tag)
    }

    override fun hideButtons() {
        mBinding.btnFinalize.visibility = View.GONE
        mBinding.btnScan.visibility = View.GONE
        mBinding.fabRecord.visibility = View.GONE
    }

    override fun showButtons() {
        mBinding.btnFinalize.visibility = View.VISIBLE
        mBinding.btnScan.visibility = View.VISIBLE
        mBinding.fabRecord.visibility = View.VISIBLE
    }

}