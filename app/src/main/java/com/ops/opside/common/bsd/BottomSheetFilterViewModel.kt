package com.ops.opside.common.bsd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetFilterViewModel @Inject constructor(
    val interactor: BottomSheetFilterInteractor
) : CommonViewModel() {
    private val _getCollectorList = MutableLiveData<MutableMap<String,String>>()
    val getCollectorList: LiveData<MutableMap<String,String>> = _getCollectorList

    private val _getMarketList = MutableLiveData<MutableMap<String,String>>()
    val getMarketList: LiveData<MutableMap<String,String>> = _getMarketList

    fun loadData(){
        getCollectors()
        getMarkets()
    }

    private fun getCollectors(){
        disposable.add(
            interactor.getCollectors().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                       _getCollectorList.value = it
                    },
                    {
                        showProgress.value = false
                    }
                )
        )
    }

    private fun getMarkets(){
        disposable.add(
            interactor.getMarkets().applySchedulers()
                .subscribe(
                    {
                        showProgress.value = false
                        _getMarketList.value = it
                    },
                    {
                        showProgress.value = false
                    }
                )
        )
    }
}