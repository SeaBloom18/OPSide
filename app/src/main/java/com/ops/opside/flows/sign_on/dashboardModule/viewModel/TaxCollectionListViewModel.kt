package com.ops.opside.flows.sign_on.dashboardModule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dashboardModule.model.TaxCollectionListInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by davidgonzalez on 15/01/23
 */
@HiltViewModel
class TaxCollectionListViewModel @Inject constructor(
    private val mTaxCollectionListInteractor: TaxCollectionListInteractor): CommonViewModel() {

    val getTaxCollectionList = MutableLiveData<MutableList<TaxCollectionSE>>()

    fun getTaxCollectionList() {
        disposable.add(
            mTaxCollectionListInteractor.getTaxCollections().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        getTaxCollectionList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}