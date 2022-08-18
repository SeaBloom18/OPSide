package com.ops.opside.flows.sign_on.concessionaireModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.concessionaireModule.model.ConcessionaireInteractor

class ConcessionaireViewModel: CommonViewModel() {
    private var mConcessionaireInteractor = ConcessionaireInteractor()
    private val _getConcessionairesList = MutableLiveData<MutableList<ConcessionaireSE>>()

    val getConcessionairesList: LiveData<MutableList<ConcessionaireSE>> = _getConcessionairesList

    fun getConcessionairesList(){
        disposable.add(
            mConcessionaireInteractor.getConcessionairesList().applySchedulers()
                .subscribe(
                    {
                        _getConcessionairesList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}