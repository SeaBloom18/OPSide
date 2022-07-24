package com.ops.opside.flows.sign_on.marketModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ops.opside.common.Entities.Market
import com.ops.opside.flows.sign_on.marketModule.marketModel.MarketInteractor

class MarketViewModel: ViewModel() {

    private var marketList: MutableList<Market>
    private var interactor: MarketInteractor

    init {
        interactor = MarketInteractor()
        marketList = mutableListOf()
    }

    private val markets: MutableLiveData<List<Market>> by lazy {
        MutableLiveData<List<Market>>().also {
            loadMarkets()
        }
    }

    fun getMarkets(): LiveData<List<Market>>{
        return markets
    }

    private fun loadMarkets(){
        interactor.getMarkets()
    }
}