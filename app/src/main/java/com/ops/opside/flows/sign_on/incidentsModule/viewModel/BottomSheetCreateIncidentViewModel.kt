package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.incidentsModule.model.BottomSheetCreateIncidentInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by davidgonzalez on 23/01/23
 */
@HiltViewModel
class BottomSheetCreateIncidentViewModel @Inject constructor(
    private val mBottomSheetCreateIncidentInteractor: BottomSheetCreateIncidentInteractor)
    : CommonViewModel() {
}