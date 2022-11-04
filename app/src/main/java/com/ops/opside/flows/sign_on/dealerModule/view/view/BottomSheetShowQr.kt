package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ops.opside.databinding.BottomSheetShowQrBinding
import com.ops.opside.flows.sign_on.dealerModule.view.viewModel.BottomSheetShowQrViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetShowQr: BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetShowQrBinding

    private val mViewModel: BottomSheetShowQrViewModel by viewModels()

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
            ivBack.setOnClickListener { dismiss() }
        }

        generateQr()
    }

    private fun generateQr() {
        val mWriter = MultiFormatWriter()

        try {
            val mMatrix = mWriter.encode(mViewModel.getFirebaseId().orEmpty(), BarcodeFormat.QR_CODE, 600, 600)
            val mEncoder = BarcodeEncoder()
            val mBitmap = mEncoder.createBitmap(mMatrix)
            mBinding.imgQr.setImageBitmap(mBitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}