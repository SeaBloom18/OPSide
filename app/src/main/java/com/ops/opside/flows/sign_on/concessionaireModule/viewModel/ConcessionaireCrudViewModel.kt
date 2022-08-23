package com.ops.opside.flows.sign_on.concessionaireModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.concessionaireModule.model.ConcessionaireCrudInteractor
import javax.inject.Inject

class ConcessionaireCrudViewModel @Inject constructor(
    private var mConcessionaireCrudInteractor: ConcessionaireCrudInteractor
): CommonViewModel() {
    private val _getMarketsList = MutableLiveData<MutableList<MarketSE>>()

    val getMarketsList: LiveData<MutableList<MarketSE>> = _getMarketsList

    fun getMarketsList(){
        disposable.add(
            mConcessionaireCrudInteractor.getMarketsList().applySchedulers()
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
}