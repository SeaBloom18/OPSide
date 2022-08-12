package com.ops.opside.flows.sign_on.marketModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.flows.sign_on.marketModule.marketModel.MarketInteractor

class MarketViewModel: ViewModel() {

    private var tianguisList: MutableList<TianguisSE>
    private var interactor: MarketInteractor

    init {
        interactor = MarketInteractor()
        tianguisList = mutableListOf()
    }

    private val markets: MutableLiveData<List<TianguisSE>> by lazy {
        MutableLiveData<List<TianguisSE>>().also {
            loadMarkets()
        }
    }

    fun getMarkets(): LiveData<List<TianguisSE>>{
        return markets
    }

    private fun loadMarkets(){
        interactor.getMarkets()
    }
}