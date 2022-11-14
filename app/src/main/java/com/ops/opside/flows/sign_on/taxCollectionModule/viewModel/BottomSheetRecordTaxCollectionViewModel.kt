package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.RecordTaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetRecordTaxCollectionViewModel @Inject constructor(
    private val mRecordTaxCollectionInteractor: RecordTaxCollectionInteractor
) : CommonViewModel() {

    private val _getEventsList = MutableLiveData<MutableList<EventRE>>()
    val getEventsList: LiveData<MutableList<EventRE>> = _getEventsList

    private val _wasEventDeleted = MutableLiveData<Boolean>()
    val wasEventDeleted: LiveData<Boolean> = _wasEventDeleted

    fun getEventsList(idTaxCollection: String) =
            mRecordTaxCollectionInteractor.getEventsList(idTaxCollection)



    fun deleteEvent(event: EventRE){
        disposable.add(
            mRecordTaxCollectionInteractor.deleteEvent(event).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _wasEventDeleted.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _wasEventDeleted.value = false
                    }
                )
        )
    }
}