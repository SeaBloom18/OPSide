package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord
import com.ops.opside.flows.sign_on.taxCollectionModule.model.RecordTaxCollectionInteractor

class BottomSheetRecordTaxCollectionViewModel: CommonViewModel() {
    private val mRecordTaxCollectionInteractor = RecordTaxCollectionInteractor()

    private val _getEventsList = MutableLiveData<MutableList<ItemRecord>>()
    val getEventsList: LiveData<MutableList<ItemRecord>> = _getEventsList

    fun getEventsList(){
        disposable.add(
            mRecordTaxCollectionInteractor.getEventsList().applySchedulers()
                .subscribe(
                    {
                        _getEventsList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

}