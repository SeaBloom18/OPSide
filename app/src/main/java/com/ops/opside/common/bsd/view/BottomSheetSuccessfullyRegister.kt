package com.ops.opside.common.bsd.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetSuccessfullyRegisterBinding


class BottomSheetSuccessfullyRegister : BaseBottomSheetFragment() {

    private val mBinding: BottomSheetSuccessfullyRegisterBinding by lazy {
        BottomSheetSuccessfullyRegisterBinding.inflate(layoutInflater)
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
            mBinding.apply {
                mBinding.btnClose.setOnClickListener { requireActivity().finish() }
            }
        }
    }
}