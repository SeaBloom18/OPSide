package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ops.opside.common.utils.PDFUtils
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetShowQrBinding
import com.ops.opside.flows.sign_on.dealerModule.view.viewModel.BottomSheetShowQrViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomSheetShowQr : BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetShowQrBinding
    private val mViewModel: BottomSheetShowQrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = BottomSheetShowQrBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            ivBack.setOnClickListener { dismiss() }
            ivPrintQR.setOnClickListener {
                PDFUtils.generatePDFBadgeSize(
                    requireContext(),
                    mutableListOf(
                        PDFUtils.qrModel(
                            mViewModel.getConcessName().orEmpty(),
                            mViewModel.getFirebaseId().orEmpty()
                        )
                    )
                )
            }
        }

        mBinding.imgQr.setImageBitmap(
            PDFUtils.generateQr(requireContext(), mViewModel.getFirebaseId().orEmpty())
        )
    }
}