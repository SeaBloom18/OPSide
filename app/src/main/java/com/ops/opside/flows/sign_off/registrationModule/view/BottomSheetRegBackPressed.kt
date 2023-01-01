package com.ops.opside.flows.sign_off.registrationModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetRegBackPressedBinding

class BottomSheetRegBackPressed: BaseBottomSheetFragment() {

    private val mBinding: BottomSheetRegBackPressedBinding by lazy {
        BottomSheetRegBackPressedBinding.inflate(layoutInflater)
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
            mBinding.btnClose.setOnClickListener { requireActivity().finish() }
        }
    }
}