package com.ops.opside.flows.sign_on.taxCollectionCrudModule.View

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.ops.opside.R
import com.ops.opside.common.Utils.Formaters
import com.ops.opside.databinding.ActivityTaxCollectionCrudBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters.TaxCollectionsCrudAdapter
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.dataClasses.TaxCollectionModel
import java.util.*


class TaxCollectionCrudActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityTaxCollectionCrudBinding
    lateinit var mAdapter: TaxCollectionsCrudAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTaxCollectionCrudBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.imgClose.setOnClickListener {
            onBackPressed()
        }

        mBinding.imgFilter.setOnClickListener {
            initBsd()
        }

        initRecyclerView()
    }

    private fun initBsd() {

        //**************** Tianguis ****************
        val itemsTianguis = mutableListOf(
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
        val hTianguis: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsTianguis)

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_filter_tax_collection, null)

        var teSearchTianguis: MaterialAutoCompleteTextView = view.findViewById(R.id.teSearchTianguis)
        var cgChipsTianguis: ChipGroup = view.findViewById(R.id.cgChipsTianguis)

        teSearchTianguis.setAdapter(hTianguis)

        teSearchTianguis.setOnItemClickListener { _, _, position, _ ->
            createChip(itemsTianguis[position], cgChipsTianguis)
            teSearchTianguis.clearFocus()
        }


        //**************** Collector ******************

        val itemsCollector = mutableListOf(
            "Recaudador 1",
            "Recaudador 2",
            "Recaudador 3",
            "Recaudador 4",
            "Recaudador 5",
            "Recaudador 6",
            "Recaudador 7",
            "Recaudador 8",
            "Recaudador 9"
        )

        val hCollector: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsCollector)

        var teSearchCollector: MaterialAutoCompleteTextView = view.findViewById(R.id.teSearchCollector)
        var cgChipsCollector: ChipGroup = view.findViewById(R.id.cgChipsCollector)

        teSearchCollector.setAdapter(hCollector)

        teSearchCollector.setOnItemClickListener { _, _, position, _ ->
            createChip(itemsCollector[position], cgChipsCollector)
            teSearchCollector.clearFocus()
        }

        //Button Calendar
        val btnCalendar: ImageButton = view.findViewById(R.id.btnCalendar)
        val etStartDate: TextInputEditText = view.findViewById(R.id.etStartDate)
        val etEndDate: TextInputEditText = view.findViewById(R.id.etEndDate)


        btnCalendar.setOnClickListener {
            openCalendar{
                etStartDate.setText(Formaters.formatDateMillis(it.first))
                etEndDate.setText(Formaters.formatDateMillis(it.second))
            }
        }

        //Button Confirm Filter
        val btnConfirmFilter: MaterialButton = view.findViewById(R.id.btnConfirmFilter)
        btnConfirmFilter.setOnClickListener { dialog.dismiss() }

        //Button Close
        val btnClose: ImageButton = view.findViewById(R.id.btnClose)
        btnClose.setOnClickListener { dialog.dismiss() }

        dialog.setContentView(view)
        dialog.show()
    }

    fun openCalendar(response: (androidx.core.util.Pair<Long,Long>) -> Unit ){
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val now = Calendar.getInstance()
        builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())

        picker.addOnNegativeButtonClickListener { }
        picker.addOnPositiveButtonClickListener { response(it) }
    }

    fun createChip(title: String, cgChipsTianguis: ChipGroup){
        val chip = Chip(this)
        chip.apply {
            text = title
            isCloseIconVisible = true
            isClickable = true
            chipBackgroundColor = ColorStateList.valueOf(getColor(R.color.secondaryColor))
            setTextColor(getColor(R.color.primaryTextColor))
        }

        chip.setOnCloseIconClickListener{
            cgChipsTianguis.removeView(chip as View)
        }

        cgChipsTianguis.addView(chip as View)
    }

    fun initRecyclerView() {
        val collections = mutableListOf<TaxCollectionModel>()

        for (i in 1..15) {
            collections.add(
                TaxCollectionModel(
                    "$i", "",
                    "Tianguis Minicipal",
                    1250.0,
                    "2022-07-12",
                    "", "", "", mutableListOf()
                )
            )
        }

        mAdapter = TaxCollectionsCrudAdapter(collections)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)

        mBinding.rvTaxCollections.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

    }

}