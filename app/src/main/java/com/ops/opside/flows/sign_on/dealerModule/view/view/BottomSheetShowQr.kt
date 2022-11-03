package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.databinding.BottomSheetShowQrBinding

class BottomSheetShowQr: BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetShowQrBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetShowQrBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            ibSignInClose.setOnClickListener { dismiss() }
        }

    }
}