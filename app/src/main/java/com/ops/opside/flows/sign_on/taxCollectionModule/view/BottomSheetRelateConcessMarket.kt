package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.databinding.BottomSheetRelateConcessMarketBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetRelateConcessMarket(
    val concessionaire: ConcessionaireSE,
    val market: MarketSE
) : BottomSheetDialogFragment() {

    private val mBinding: BottomSheetRelateConcessMarketBinding by lazy {
        BottomSheetRelateConcessMarketBinding.inflate(layoutInflater)
    }

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
            ibClose.setOnClickListener { dismiss() }

            etDealerName.setText(concessionaire.name)
            etMarketName.setText(market.name)
        }
    }

}