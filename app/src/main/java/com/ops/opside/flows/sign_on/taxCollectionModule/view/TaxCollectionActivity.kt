package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.ops.opside.R
import com.ops.opside.common.Utils.animateOnPress
import com.ops.opside.common.Utils.tryOrPrintException
import com.ops.opside.common.adapterCallback.SwipeToDeleteCallback
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.databinding.ActivityTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ADDED
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.FLOOR_COLLECTION
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.PENALTY_FEE
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.RecordTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord
import com.ops.opside.flows.sign_on.taxCollectionModule.interfaces.TaxCollectionAux

class TaxCollectionActivity : AppCompatActivity(), TaxCollectionAux {

    private lateinit var mBinding: ActivityTaxCollectionBinding
    private lateinit var mAdapter: RecordTaxCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTaxCollectionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {

            fabRecord.animateOnPress()
            fabRecord.setOnClickListener {
                bottomSheet()
            }

            btnFinalize.setOnClickListener {
                val dialog = BaseDialog(
                    this@TaxCollectionActivity,
                    getString(R.string.common_atention),
                    getString(R.string.tax_collection_finalize_collection),
                    getString(R.string.common_accept),
                    "",
                    { launchFinalizeFragment() },
                    {  },
                )
                dialog.show()
            }
        }


        setUpPieChart()
        setUpBottomSheetPickTianguis()
    }

    private fun launchFinalizeFragment() {
        tryOrPrintException {
            val fragment = FinalizeTaxCollectionFragment()

            val mBundle = Bundle()
            mBundle.putString("type", "create")
            fragment.arguments = mBundle

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.add(R.id.containerTaxCollection, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun setUpBottomSheetPickTianguis() {
        val items = mutableListOf(
            "Tianguis 1",
            "Tianguis 2",
            "Tianguis 3",
            "Tianguis 4",
            "Tianguis 5",
            "Tianguis 6",
            "Tianguis 7",
            "Tianguis 8",
            "Tianguis 9"
        )
        val h: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items)

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_pick_tianguis, null)

        var spinner: MaterialAutoCompleteTextView = view.findViewById(R.id.spPickTianguis)
        spinner.setAdapter(h)

        view.findViewById<MaterialButton>(R.id.btnPickTianguis).setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setUpPieChart() {
        mBinding.chartTaxMoney.apply {
            // Set Progress
            progress = 65f
            // or with animation
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


    private fun bottomSheet() {
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