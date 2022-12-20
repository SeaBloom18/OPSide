package com.ops.opside.flows.sign_on.concessionaireModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.concessionaireModule.model.ConcessionaireInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConcessionaireViewModel @Inject constructor(
    private val mConcessionaireInteractor: ConcessionaireInteractor
): CommonViewModel() {
    private val _getConcessionairesList = MutableLiveData<MutableList<ConcessionaireSE>>()

    val getConcessionairesList: LiveData<MutableList<ConcessionaireSE>> = _getConcessionairesList

    fun getConcessionairesList(){
        disposable.add(
            mConcessionaireInteractor.getConcessionairesList().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getConcessionairesList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getConcessByMarketList(markets: MutableList<String>){
        disposable.add(
            mConcessionaireInteractor.getConcessByMarketList(markets).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getConcessionairesList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}