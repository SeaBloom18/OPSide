package com.ops.opside.flows.sign_on.concessionaireModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.concessionaireModule.model.ConcessionaireInteractor

class ConcessionaireViewModel: CommonViewModel() {

    private val mConcessionaireInteractor = ConcessionaireInteractor()
    val getConcessionaireList = MutableLiveData<MutableList<ConcessionaireSE>>()

    fun getConcessionaireList() {
        disposable.add(
            mConcessionaireInteractor.getConcessionaires().applySchedulers()
                .subscribe(
                    {
                        getConcessionaireList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}