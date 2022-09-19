package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.RelateConcessMarketInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetRelateConcessMarketViewModel @Inject constructor(
    val mRelateConcessMarketInteractor: RelateConcessMarketInteractor
) : CommonViewModel() {

    private val _addRelate = MutableLiveData<String>()
    val addRelate: LiveData<String> = _addRelate

    private val _persistParticipatingConcess = MutableLiveData<Boolean>()
    val persistParticipatingConcess: LiveData<Boolean> = _persistParticipatingConcess

    fun relateConcessWithMarket(participatingConcess: ParticipatingConcessRE) {
        disposable.add(
            mRelateConcessMarketInteractor.relateConcessWithMarket(participatingConcess)
                .applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _addRelate.value = it.orEmpty()
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _addRelate.value = ""
                    }
                )
        )
    }

    fun persistParticipatingConcess(participatingConcess: ParticipatingConcessRE) {
        disposable.add(
            mRelateConcessMarketInteractor.persistParticipatingConcess(participatingConcess)
                .applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _persistParticipatingConcess.value = it!!
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _persistParticipatingConcess.value = false
                    }
                )
        )
    }

}