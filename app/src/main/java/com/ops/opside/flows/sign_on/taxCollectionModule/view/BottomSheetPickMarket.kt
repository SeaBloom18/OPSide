package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.R
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.BottomSheetPickTianguisBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetPickMarketViewModel

class BottomSheetPickMarket(
    private val selection: (TianguisSE) -> Unit = {}
) : BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetPickTianguisBinding
    private lateinit var mViewModel: BottomSheetPickMarketViewModel
    private lateinit var mActivity: TaxCollectionActivity
    private lateinit var mMarketsList: MutableList<TianguisSE>
    private var mSelectedMarket: TianguisSE? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetPickTianguisBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            btnPickMarket.setOnClickListener {
                returnSelectedMarket()
            }

            ibSignInClose.animateOnPress()
            ibSignInClose.setOnClickListener { mActivity.onBackPressed() }
        }

        setUpActivity()
        bindViewModel()
        loadMarketsList()
    }

    private fun returnSelectedMarket() {
        mSelectedMarket = searchSelectedMarket()

        if (mSelectedMarket != null){
            selection.invoke(mSelectedMarket!!)
            dismiss()
        } else{
            Toast.makeText(mActivity, getString(R.string.tax_collection_choose_tianguis),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpActivity() {
        mActivity = activity as TaxCollectionActivity
    }

    private fun searchSelectedMarket(): TianguisSE? {
        for (item in mMarketsList){
            if (mBinding.spPickMarket.text.toString() == item.name){
                return item
            }
        }

        return null
    }

    private fun bindViewModel(){
        mViewModel = ViewModelProvider(requireActivity())[BottomSheetPickMarketViewModel::class.java]

        mViewModel.getMarketsList.observe(this, Observer(this::getMarketsList))
    }

    private fun loadMarketsList(){
        mViewModel.getMarketsList()
    }

    private fun setUpSpinner() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, getMarketsListNames())
       mBinding.spPickMarket.setAdapter(adapter)
    }

    private fun getMarketsListNames(): MutableList<String> {
        return mMarketsList.map { it.name }.toMutableList()
    }


    private fun getMarketsList(marketsList: MutableList<TianguisSE>) {
        mMarketsList = marketsList
        setUpSpinner()
    }


}