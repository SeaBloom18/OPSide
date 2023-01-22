package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.incidentsModule.model.CreateIncidentsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by davidgonzalez on 22/01/23
 */
@HiltViewModel
class CreateIncidentsViewModel @Inject constructor(
    private val mCreateIncidentsInteractor: CreateIncidentsInteractor): CommonViewModel() {

    private val _getConcessionairesList = MutableLiveData<MutableList<ConcessionaireSE>>()
    val getConcessionairesList: LiveData<MutableList<ConcessionaireSE>> = _getConcessionairesList

    fun getConcessionairesList(){
        disposable.add(
            mCreateIncidentsInteractor.getConcessionaireList().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getConcessionairesList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}