package com.ops.opside.flows.sign_on.dashboardModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dashboardModule.model.ControlPanelInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 10/11/22.
 */
@HiltViewModel
class ControlPanelViewModel @Inject constructor(
    private val mControlPanelInteractor: ControlPanelInteractor): CommonViewModel(){

    val getCollectorList = MutableLiveData<MutableList<CollectorSE>>()

    fun getCollectorList() {
        disposable.add(
            mControlPanelInteractor.getCollectors().applySchedulers()
                .subscribe(
                    {
                        getCollectorList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

}