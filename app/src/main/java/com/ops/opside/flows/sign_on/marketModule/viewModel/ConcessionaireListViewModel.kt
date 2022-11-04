package com.ops.opside.flows.sign_on.marketModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.marketModule.model.ConcessionaireListInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 11/10/22.
 */
@HiltViewModel
class ConcessionaireListViewModel @Inject constructor(
    private val mConcessionaireListInteractor: ConcessionaireListInteractor): CommonViewModel(){

    private val marketFirestoreId = MutableLiveData<String>()
    val getMarketFirestoreId: LiveData<String> = marketFirestoreId

    fun getMarketId(marketId: String){
        disposable.add(
            mConcessionaireListInteractor.getConcessionaireList(marketId).applySchedulers()
                .subscribe(
                    {
                        marketFirestoreId.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}