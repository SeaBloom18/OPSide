package com.ops.opside.flows.sign_on.dashboardModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.databinding.BottomSheetAnalyticsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by davidgonzalez on 17/01/23
 */
@AndroidEntryPoint
class BottomSheetAnalytics: BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetAnalyticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetAnalyticsBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}