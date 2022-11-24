package com.ops.opside.common.bsd

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.ops.opside.R
import com.ops.opside.common.utils.Formaters
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.databinding.BottomSheetFilterBinding
import java.util.Calendar


class BottomSheetFilter: BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetFilterBinding
    private lateinit var mMarketList: MutableList<String>
    private lateinit var mCollectorsList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetFilterBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {

            // *********** Market ***********
            val hMarket: ArrayAdapter<String> =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item
                    , getMarketRegistered())

            teSearchMarket.setAdapter(hMarket)

            teSearchMarket.setOnItemClickListener { _, _, position, _ ->
                createChip(mMarketList[position], cgChipsMarket)
                teSearchMarket.clearFocus()
            }

            // *********** Collectors ***********
            val hCollector: ArrayAdapter<String> =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item,
                    getCollectorsRegistered())

            teSearchCollector.setAdapter(hCollector)

            teSearchCollector.setOnItemClickListener { _, _, position, _ ->
                createChip(mCollectorsList[position], cgChipsCollector)
                teSearchCollector.clearFocus()
            }


            // *********** Buttons ***********
            btnClose.animateOnPress()
            btnClose.setOnClickListener {
                dismiss()
            }

            btnCalendar.animateOnPress()
            btnCalendar.setOnClickListener {
                openCalendar{
                    etStartDate.setText(Formaters.formatDateMillis(it.first))
                    etEndDate.setText(Formaters.formatDateMillis(it.second))
                }
            }

            btnConfirmFilter.setOnClickListener { dismiss() }
        }
    }

    private fun getMarketRegistered(): MutableList<String> {
         mMarketList = mutableListOf()
        for (i in 1..10){
            mMarketList.add("Market $i")
        }
        return mMarketList
    }

    private fun getCollectorsRegistered(): MutableList<String> {
        mCollectorsList = mutableListOf()
        for (i in 1..10){
            mCollectorsList.add("Recaudador $i")
        }
        return mCollectorsList
    }

    private fun createChip(title: String, cgChipsMarket: ChipGroup){
        tryOrPrintException {
            val chip = Chip(context)
            chip.apply {
                text = title
                isCloseIconVisible = true
                isClickable = true
                chipBackgroundColor = ColorStateList.valueOf(context.getColor(R.color.secondaryColor))
                setTextColor(context.getColor(R.color.primaryTextColor))
            }

            chip.setOnCloseIconClickListener{
                cgChipsMarket.removeView(chip as View)
            }

            cgChipsMarket.addView(chip as View)
        }
    }

    private fun openCalendar(response: (androidx.core.util.Pair<Long,Long>) -> Unit ){
        tryOrPrintException {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val now = Calendar.getInstance()
            builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

            val picker = builder.build()
            picker.show((context as FragmentActivity).supportFragmentManager, picker.toString())

            picker.addOnNegativeButtonClickListener { }
            picker.addOnPositiveButtonClickListener { response(it) }
        }
    }
}