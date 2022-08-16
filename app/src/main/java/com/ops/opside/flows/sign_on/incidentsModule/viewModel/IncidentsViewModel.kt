package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.incidentsModule.model.IncidentsInteractor

class IncidentsViewModel: CommonViewModel() {

    private val mIncidentsInteractor = IncidentsInteractor()
    val getIncidentsPersonList = MutableLiveData<MutableList<IncidentPersonFE>>()

    fun getIncidentsPersonList(){
        disposable.add(
            mIncidentsInteractor.getIncidentsPersonsList().applySchedulers()
                .subscribe(
                    {
                        getIncidentsPersonList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}