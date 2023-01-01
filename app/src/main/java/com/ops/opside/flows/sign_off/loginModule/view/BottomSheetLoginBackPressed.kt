package com.ops.opside.flows.sign_off.loginModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ops.opside.R
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetLoginBackpressedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetLoginBackPressed: BaseBottomSheetFragment() {

    private val mBinding: BottomSheetLoginBackpressedBinding by lazy {
        BottomSheetLoginBackpressedBinding.inflate(layoutInflater)
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
            mBinding.btnClose.setText(R.string.login_btn_close_bottom_sheet)
            mBinding.tvBSTitle.setText(R.string.login_tv_title_bottom_sheet)

        }
    }
}