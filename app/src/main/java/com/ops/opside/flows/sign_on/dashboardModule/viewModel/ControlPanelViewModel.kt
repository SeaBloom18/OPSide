package com.ops.opside.flows.sign_on.dashboardModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
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
    private val _getPriceLinearMeter = MutableLiveData<String>()
    val priceLinearMeter: LiveData<String> = _getPriceLinearMeter
    private val updatePrice = MutableLiveData<Boolean>()
    private val updateHasAccess = MutableLiveData<Boolean>()


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

    fun getPriceLinearMeter(){
        disposable.add(
            mControlPanelInteractor.getLinealPriceMeter().applySchedulers()
                .subscribe(
                    {
                        _getPriceLinearMeter.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun updateLinealPriceMeter(idFirestore: String, price: String): Boolean {
        var updateSuccess = false
        disposable.add(
            mControlPanelInteractor.updateLinealPriceMeter(idFirestore, price).applySchedulers()
                .subscribe(
                    {
                        updatePrice.value = it
                        updateSuccess = true
                    },
                    {
                        Log.e("Error", it.toString())
                        updateSuccess = false
                    }
                )
        )
        return updateSuccess
    }

    fun updateHasAccess(idFirestore: String, hasAccess: Boolean){
        disposable.add(
            mControlPanelInteractor.updateHasAccess(idFirestore, hasAccess).applySchedulers()
                .subscribe(
                    {
                        updateHasAccess.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}