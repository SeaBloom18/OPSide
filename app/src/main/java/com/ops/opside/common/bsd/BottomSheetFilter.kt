package com.ops.opside.common.bsd

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.ops.opside.R
import com.ops.opside.common.bsd.viewModel.BottomSheetFilterViewModel
import com.ops.opside.common.utils.*
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFilter(
    val showMarket: Boolean,
    val showCollector: Boolean,
    val showDate: Boolean
) : BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetFilterBinding
    private val mViewModel: BottomSheetFilterViewModel by viewModels()

    private var mCollectors = mutableMapOf<String, String>()
    private var mMarkets = mutableMapOf<String, String>()

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

            btnClose.animateOnPress()
            btnClose.setOnClickListener {
                dismiss()
            }

            btnCalendar.animateOnPress()
            btnCalendar.setOnClickListener {
                openCalendar {
                    etStartDate.setText(Formaters.formatDateMillis(it.first + DAY_IN_MILLIS))
                    etEndDate.setText(Formaters.formatDateMillis(it.second + DAY_IN_MILLIS))
                }
            }

            btnConfirmFilter.setOnClickListener {
                dismiss()
                setFragmentResult(
                    KEY_FILTER_REQUEST,
                    bundleOf().apply {
                        putStringArrayList(
                            "market",
                            (getChipsList(mBinding.cgChipsMarket).map { mMarkets[it] })
                                    as ArrayList<String>
                        )

                        putStringArrayList(
                            "collector",
                            (getChipsList(mBinding.cgChipsCollector).map { mCollectors[it] })
                                    as ArrayList<String>
                        )

                        putString("startDate", etStartDate.text.toString())
                        putString("endDate", etEndDate.text.toString())
                    }
                )
            }
        }

        bindViewModel()
        setUpViews(showMarket, showCollector, showDate)
        mViewModel.loadData()
    }

    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(requireActivity(), Observer(this::showLoading))

        mViewModel.getMarketList.observe(
            requireActivity(),
            Observer(this::getMarketRegistered)
        )

        mViewModel.getCollectorList.observe(
            requireActivity(),
            Observer(this::getCollectorsRegistered)
        )
    }

    private fun setUpViews(showMarket: Boolean, showCollector: Boolean, showDate: Boolean) {
        mBinding.apply {
            teSearchMarket.isGone = showMarket.not()
            tilSearchMarket.isGone = showMarket.not()

            tilSearchCollector.isGone = showCollector.not()

            btnCalendar.isGone = showDate.not()
            txtDate.isGone = showDate.not()
            etStartDate.isGone = showDate.not()
            etEndDate.isGone = showDate.not()
        }
    }

    private fun getMarketRegistered(markets: MutableMap<String, String>) {
        mMarkets = markets
        val list = markets.map { it.key }
        val hMarket: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_list_item_1, list
            )

        mBinding.apply {
            teSearchMarket.setAdapter(hMarket)

            teSearchMarket.setOnItemClickListener { _, _, position, _ ->
                createChip(list[position], cgChipsMarket)
                teSearchMarket.clearFocus()
            }
        }
    }

    private fun getCollectorsRegistered(collectors: MutableMap<String, String>) {
        mCollectors = collectors
        val list = collectors.map { it.key }
        val hCollector: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_list_item_1, list
            )

        mBinding.apply {
            teSearchCollector.setAdapter(hCollector)

            teSearchCollector.setOnItemClickListener { _, _, position, _ ->
                createChip(list[position], cgChipsCollector)
                teSearchCollector.clearFocus()
            }
        }
    }

    private fun createChip(title: String, cgChipsMarket: ChipGroup) {
        tryOrPrintException {
            val chip = Chip(context)
            chip.apply {
                text = title
                isCloseIconVisible = true
                isClickable = true
                chipBackgroundColor =
                    ColorStateList.valueOf(context.getColor(R.color.secondaryColor))
                setTextColor(context.getColor(R.color.primaryTextColor))
            }

            chip.setOnCloseIconClickListener {
                cgChipsMarket.removeView(chip as View)
            }

            cgChipsMarket.addView(chip as View)
        }
    }

    private fun openCalendar(response: (androidx.core.util.Pair<Long, Long>) -> Unit) {
        tryOrPrintException {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setTheme(R.style.MaterialCalendarTheme)
            val now = CalendarUtils.getCurrentTimeInMillis()
            builder.setSelection(androidx.core.util.Pair(now, now))

            val picker = builder.build()
            picker.show((context as FragmentActivity).supportFragmentManager, picker.toString())

            picker.addOnNegativeButtonClickListener { }
            picker.addOnPositiveButtonClickListener { response(it) }
        }
    }

    private fun getChipsList(chipGroup: ChipGroup): MutableList<String> {
        val list = mutableListOf<String>()
        chipGroup.children
            .toList()
            .map { list.add((it as Chip).text.toString()) }
        return list
    }
}

const val KEY_FILTER_REQUEST = "KEY_FILTER_REQUEST"