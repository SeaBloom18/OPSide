package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence
import com.ops.opside.flows.sign_on.taxCollectionModule.model.FinalizeTaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinalizeTaxCollectionViewModel @Inject constructor(
    private val mFinalizeTaxCollectionInteractor: FinalizeTaxCollectionInteractor
): CommonViewModel() {

    private val _getAbsencesList = MutableLiveData<MutableList<ItemAbsence>>()
    val getAbsencesList: LiveData<MutableList<ItemAbsence>> = _getAbsencesList

    fun getAbsencesList(){
        disposable.add(
            mFinalizeTaxCollectionInteractor.getAbsenceList().applySchedulers()
                .subscribe(
                    {
                        _getAbsencesList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

}