package com.ops.opside.flows.sign_on.taxCollectionCrudModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.model.TaxCollectionCrudInteractor

class TaxCollectionCrudViewModel: CommonViewModel() {

    private var mTaxCollectionCrudInteractor = TaxCollectionCrudInteractor()
    private val _getCollectionsList = MutableLiveData<MutableList<TaxCollectionSE>>()

    val getCollectionsList: LiveData<MutableList<TaxCollectionSE>> = _getCollectionsList

    fun getCollectionsList(){
        disposable.add(
            mTaxCollectionCrudInteractor.getCollectionList().applySchedulers()
                .subscribe(
                    {
                        _getCollectionsList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

}