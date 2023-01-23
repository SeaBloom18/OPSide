package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import android.util.Log
import com.ops.opside.common.entities.firestore.IncidentFE
import com.ops.opside.common.utils.applySchedulers
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

        fun insertIncident(incidentFE: IncidentFE) {
            disposable.add(
                mBottomSheetCreateIncidentInteractor.insertIncident(incidentFE).applySchedulers()
                    .doOnSubscribe { showProgress.value = true }
                    .subscribe(
                        {
                            //_registerConcessionaire.value = it
                            showProgress.value = false
                            //_action.value = RegistrationAction.ShowMessageSuccess
                        },
                        {
                            //_registerConcessionaire.value = false
                            Log.e("Error", it.toString())
                            showProgress.value = false
                            //_action.value = RegistrationAction.ShowMessageError
                        }
                    )
            )
        }
}