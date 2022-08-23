package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.PickMarketInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetPickMarketViewModel @Inject constructor(
    private val mPickMarketsInteractor: PickMarketInteractor
) : CommonViewModel() {

    private val _getMarketsList = MutableLiveData<MutableList<MarketFE>>()

    val getMarketsList: LiveData<MutableList<MarketFE>> = _getMarketsList

    fun getMarketsList(){
        disposable.add(
            mPickMarketsInteractor.getMarketsList().applySchedulers()
                .subscribe(
                    {
                        _getMarketsList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun putIsOnLineMode(isOnLineMode: Boolean){
        mPickMarketsInteractor.putIsOnLineMode(isOnLineMode)
    }

    fun isOnLineMode(): Boolean{
        return mPickMarketsInteractor.isOnLineMode()
    }

}