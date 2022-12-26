package com.ops.opside.flows.sign_on.dealerModule.view.viewModel

import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dealerModule.view.model.ShowQrInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetShowQrViewModel @Inject constructor(
    private val mShowQrInteractor: ShowQrInteractor
): CommonViewModel(){
    fun getFirebaseId() = mShowQrInteractor.getFirebaseId()
    fun getConcessName() = mShowQrInteractor.getConcesName()
}