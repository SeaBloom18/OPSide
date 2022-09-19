package com.ops.opside.flows.sign_on.marketModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.marketModule.model.MarketInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val mMarketInteractor: MarketInteractor
): CommonViewModel() {

    val getMarketList = MutableLiveData<MutableList<MarketSE>>()

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
}