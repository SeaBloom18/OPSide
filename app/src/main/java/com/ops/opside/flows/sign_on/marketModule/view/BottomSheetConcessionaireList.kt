package com.ops.opside.flows.sign_on.marketModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.databinding.BottomSheetPickMarketBinding
import com.ops.opside.databinding.BottomSheetShowConcessBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

/**
 * Created by David Alejandro González Quezada on 10/10/22.
 */
class BottomSheetConcessionaireList: BottomSheetDialogFragment() {

    private val mBinding: BottomSheetShowConcessBinding by lazy {
        BottomSheetShowConcessBinding.inflate(layoutInflater)
    }
    private val mActivity: MarketRegisterActivity by lazy {
        activity as MarketRegisterActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}