package com.ops.opside.flows.sign_on.taxCollectionCrudModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.model.TaxCollectionCrudInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaxCollectionCrudViewModel @Inject constructor(
    private var mTaxCollectionCrudInteractor: TaxCollectionCrudInteractor
): CommonViewModel() {

    private val _getCollectionsList = MutableLiveData<MutableList<TaxCollectionSE>>()

    val getCollectionsList: LiveData<MutableList<TaxCollectionSE>> = _getCollectionsList

    fun getCollectionsList(){
        disposable.add(
            mTaxCollectionCrudInteractor.getCollectionList().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getCollectionsList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

}