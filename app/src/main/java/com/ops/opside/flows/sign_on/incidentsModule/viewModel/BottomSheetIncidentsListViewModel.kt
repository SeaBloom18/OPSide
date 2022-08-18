package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.incidentsModule.model.ListIncidentsInteractor

class BottomSheetIncidentsListViewModel: CommonViewModel() {

    private val mBottomSheetIncidentsInteractor = ListIncidentsInteractor()
    val getIncidentsList = MutableLiveData<MutableList<IncidentSE>>()

    fun getIncidentsList(){
        disposable.add(
            mBottomSheetIncidentsInteractor.getIncidentsList().applySchedulers()
                .subscribe(
                    {
                        getIncidentsList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}