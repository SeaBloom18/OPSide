package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.BottomSheetForeignerAttendanceInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetForeignerAttendanceViewModel @Inject constructor(
    val mInteractor: BottomSheetForeignerAttendanceInteractor
): CommonViewModel() {

    private val _getEmailInformation = MutableLiveData<ConcessionaireSE?>()
    val getEmailInformation: LiveData<ConcessionaireSE?> = _getEmailInformation


    fun getEmailInformation(email: String){
        disposable.add(
            mInteractor.getEmailInformation(email).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getEmailInformation.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())

                        _getEmailInformation.value = null
                    }
                )
        )
    }

}