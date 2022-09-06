package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.BottomSheetPickMarketBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetPickMarketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetPickMarket(
    private val selection: (MarketFE) -> Unit = {}
) : BottomSheetDialogFragment() {

    private val mBinding: BottomSheetPickMarketBinding by lazy {
        BottomSheetPickMarketBinding.inflate(layoutInflater)
    }

    private val mActivity: TaxCollectionActivity by lazy {
        activity as TaxCollectionActivity
    }

    private val mViewModel: BottomSheetPickMarketViewModel by viewModels()

    private lateinit var mMarketsList: MutableList<MarketFE>
    private var mSelectedMarket: MarketFE? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {

            if (mViewModel.isOnLineMode())
                rbOnLine.isChecked = true
            else
                rbOffLine.isChecked = true

            rbOnLine.setOnClickListener { changeLineMode(true) }
            rbOffLine.setOnClickListener { changeLineMode(false) }

            btnPickMarket.setOnClickListener { returnSelectedMarket() }

            ibClose.animateOnPress()
            ibClose.setOnClickListener { mActivity.onBackPressed() }
        }

        bindViewModel()
        loadMarketsList()
    }

    private fun changeLineMode(isOnLineMode: Boolean) {
        mBinding.rbOnLine.isChecked = isOnLineMode
        mBinding.rbOffLine.isChecked = isOnLineMode.not()

        mViewModel.putIsOnLineMode(isOnLineMode)
    }

    private fun returnSelectedMarket() {
        mSelectedMarket = searchSelectedMarket()

        if (mSelectedMarket != null){
            selection.invoke(mSelectedMarket!!)
            dismiss()
        } else{
            Toast.makeText(mActivity, getString(R.string.tax_collection_choose_market),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchSelectedMarket(): MarketFE? {
        for (item in mMarketsList){
            if (mBinding.spPickMarket.text.toString() == item.name){
                return item
            }
        }

        return null
    }

    private fun bindViewModel(){
        mViewModel.getMarketsList.observe(this, Observer(this::getMarketsList))
    }

    private fun loadMarketsList(){
        mViewModel.getMarketsList()
    }

    private fun setUpSpinner() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, getMarketsListNames())
       mBinding.spPickMarket.setAdapter(adapter)
    }

    private fun getMarketsListNames(): MutableList<String> {
        return mMarketsList.map { it.name }.toMutableList()
    }


    private fun getMarketsList(marketsList: MutableList<MarketFE>) {
        mMarketsList = marketsList
        setUpSpinner()
    }
}