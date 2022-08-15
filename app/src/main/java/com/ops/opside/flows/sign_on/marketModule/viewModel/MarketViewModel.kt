package com.ops.opside.flows.sign_on.marketModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.marketModule.model.MarketInteractor

class MarketViewModel: CommonViewModel() {

    private val mMarketInteractor = MarketInteractor()
    val getMarketList = MutableLiveData<MutableList<TianguisSE>>()

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