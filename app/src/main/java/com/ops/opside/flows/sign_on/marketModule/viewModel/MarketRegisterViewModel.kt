package com.ops.opside.flows.sign_on.marketModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dashboardModule.actions.ControlPanelAction
import com.ops.opside.flows.sign_on.marketModule.actions.MarketAction
import com.ops.opside.flows.sign_on.marketModule.model.MarketRegisterInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 04/09/22.
 */
@HiltViewModel
class MarketRegisterViewModel @Inject constructor(
    private val mMarketRegisterInteractor: MarketRegisterInteractor): CommonViewModel() {

    private val registerMarket = MutableLiveData<Boolean>()
    private val getMarketData = MutableLiveData<MutableList<String>>()

    private val _action: SingleLiveEvent<MarketAction> = SingleLiveEvent()
    fun getAction(): LiveData<MarketAction> = _action

    fun insertMarket(marketFE: MarketFE){
        disposable.add(
            mMarketRegisterInteractor.registerMarket(marketFE).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _action.value = MarketAction.ShowMessageSuccess
                        registerMarket.value = it
                        showProgress.value = false
                    },
                    {
                        _action.value = MarketAction.ShowMessageError
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun updateMarket(idFirestore: String, name: String, address: String, marketMeters: Double, latitude: Double,
                     longitude: Double){
        disposable.add(
            mMarketRegisterInteractor.updateMarket(idFirestore, name, address, marketMeters, latitude, longitude).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _action.value = MarketAction.ShowMessageSuccess
                        registerMarket.value = it
                        showProgress.value = false
                    },
                    {
                        _action.value = MarketAction.ShowMessageError
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getConcessionairesForMarket(idMarketFirestore: String){
        disposable.add(
            mMarketRegisterInteractor.getConcessionairesForMarket(idMarketFirestore).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        getMarketData.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}