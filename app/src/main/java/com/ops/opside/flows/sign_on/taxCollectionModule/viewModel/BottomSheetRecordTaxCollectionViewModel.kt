package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord
import com.ops.opside.flows.sign_on.taxCollectionModule.model.RecordTaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetRecordTaxCollectionViewModel @Inject constructor(
    private val mRecordTaxCollectionInteractor: RecordTaxCollectionInteractor
): CommonViewModel() {

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