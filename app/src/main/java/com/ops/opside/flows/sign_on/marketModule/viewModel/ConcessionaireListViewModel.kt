package com.ops.opside.flows.sign_on.marketModule.viewModel

import android.util.Log
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

    val marketFirestoreId = MutableLiveData<String>()

    fun getMarketId(marketId: String){
        disposable.add(
            mConcessionaireListInteractor.getConcessionaireList(marketId).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        marketFirestoreId.value = it
                        showProgress.value = false
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                    }
                )
        )
    }
}