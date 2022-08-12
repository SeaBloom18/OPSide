package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.PickTianguisInteractor

class BottomSheetPickTianguisViewModel: CommonViewModel() {
    private var mPickTianguisInteractor = PickTianguisInteractor()
    private val _getTianguisList = MutableLiveData<MutableList<TianguisSE>>()

    val getTianguisList: LiveData<MutableList<TianguisSE>> = _getTianguisList

    fun getTianguisList(){
        disposable.add(
            mPickTianguisInteractor.getTianguisList().applySchedulers()
                .subscribe(
                    {
                        _getTianguisList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}