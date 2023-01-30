package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.incidentsModule.model.IncidentsAssignedInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by davidgonzalez on 30/01/23
 */
@HiltViewModel
class IncidentsAssignedViewModel @Inject constructor(
    private val mIncidentsAssignedInteractor: IncidentsAssignedInteractor): CommonViewModel(){

    val _getIncidentPersonList = MutableLiveData<MutableList<IncidentPersonFE>>()

    fun getTaxCollectionList() {
        disposable.add(
            mIncidentsAssignedInteractor.getIncidentsAssigned().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getIncidentPersonList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}