package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.ops.opside.R
import com.ops.opside.common.adapterCallback.SwipeToDeleteCallback
import com.ops.opside.databinding.ActivityTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ADDED
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.FLOOR_COLLECTION
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.PENALTY_FEE
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.RecordTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.pojos.ItemRecord

class TaxCollectionActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityTaxCollectionBinding
    private lateinit var mAdapter: RecordTaxCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTaxCollectionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fabRecord.setOnClickListener {
            bottomSheet()
        }

        setUpPieChart()
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
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

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
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_record_tax_collection, null)

        val events = mutableListOf<ItemRecord>()

        events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:25 PM", 25.0))
        events.add(ItemRecord(FLOOR_COLLECTION, "Citlaly García Razo", "04:27 PM", 54.5))
        events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:29 PM", 34.0))
        events.add(ItemRecord(ADDED, "Julio Zepeda", "04:30 PM"))
        events.add(ItemRecord(FLOOR_COLLECTION, "Julio Zepeda", "04:31 PM", 34.0))
        events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:32 PM", 25.0))
        events.add(ItemRecord(PENALTY_FEE, "Citlaly García Razo", "04:33 PM"))
        events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:35 PM", 34.0))

        mAdapter = RecordTaxCollectionAdapter(events)

        var recyclerView = view.findViewById<RecyclerView>(R.id.rvRecord)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

        dialog.setContentView(view)
        dialog.show()

        val swipeHandler = object : SwipeToDeleteCallback(this@TaxCollectionActivity) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.events.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }




}