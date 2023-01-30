package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.incidentsModule.model.IncidentsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by davidgonzalez on 30/01/23
 */
@HiltViewModel
class InteractorViewModel @Inject constructor(
    private val mIncidentsInteractor: IncidentsInteractor): CommonViewModel(){
}