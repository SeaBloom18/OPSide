package com.ops.opside.flows.sign_on.dashboardModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.registrationModule.actions.RegistrationAction
import com.ops.opside.flows.sign_on.dashboardModule.actions.ControlPanelAction
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

    private val _getPriceLinearMeter = MutableLiveData<Float>()
    val priceLinearMeter: LiveData<Float> = _getPriceLinearMeter

    private val updatePrice = MutableLiveData<Boolean>()
    private val updateHasAccess = MutableLiveData<Boolean>()

    private val _action: SingleLiveEvent<ControlPanelAction> = SingleLiveEvent()
    fun getAction(): LiveData<ControlPanelAction> = _action


    fun getCollectorList() {
        disposable.add(
            mControlPanelInteractor.getCollectors().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        getCollectorList.value = it
                        showProgress.value = false
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getPriceLinearMeter(){
        disposable.add(
            mControlPanelInteractor.getLinealPriceMeter().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _getPriceLinearMeter.value = it
                        showProgress.value = false
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun updateLinealPriceMeter(idFirestore: String, price: String) {
        disposable.add(
            mControlPanelInteractor.updateLinealPriceMeter(idFirestore, price).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        updatePrice.value = it
                        showProgress.value = false
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun updateHasAccess(idFirestore: String, hasAccess: Boolean){
        disposable.add(
            mControlPanelInteractor.updateHasAccess(idFirestore, hasAccess).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _action.value = ControlPanelAction.ShowMessageSuccess
                        updateHasAccess.value = it
                    },
                    {
                        _action.value = ControlPanelAction.ShowMessageError
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}