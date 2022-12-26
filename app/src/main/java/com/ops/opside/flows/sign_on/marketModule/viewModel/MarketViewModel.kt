package com.ops.opside.flows.sign_on.marketModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dashboardModule.actions.ControlPanelAction
import com.ops.opside.flows.sign_on.marketModule.actions.MarketAction
import com.ops.opside.flows.sign_on.marketModule.model.MarketInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val mMarketInteractor: MarketInteractor
): CommonViewModel() {

    val getMarketList = MutableLiveData<MutableList<MarketSE>>()
    private val deleteMarket = MutableLiveData<Boolean>()

    private val _action: SingleLiveEvent<MarketAction> = SingleLiveEvent()
    fun getAction(): LiveData<MarketAction> = _action

    fun getMarketList(){
        disposable.add(
            mMarketInteractor.getMarkets().applySchedulers()
                .subscribe(
                    {
                        getMarketList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun deleteMarket(idFirestore: String){
        disposable.add(
            mMarketInteractor.deleteMarket(idFirestore).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        deleteMarket.value = it
                        _action.value = MarketAction.ShowMessageSuccess
                    },
                    {
                        showProgress.value = false
                        _action.value = MarketAction.ShowMessageError
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}