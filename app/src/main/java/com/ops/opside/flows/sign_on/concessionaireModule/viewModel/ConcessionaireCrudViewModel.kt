package com.ops.opside.flows.sign_on.concessionaireModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.concessionaireModule.model.ConcessionaireCrudInteractor

class ConcessionaireCrudViewModel : CommonViewModel() {
    private var mConcessionaireCrudInteractor = ConcessionaireCrudInteractor()
    private val _getMarketsList = MutableLiveData<MutableList<TianguisSE>>()

    val getMarketsList: LiveData<MutableList<TianguisSE>> = _getMarketsList

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