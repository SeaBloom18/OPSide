package com.ops.opside.flows.sign_on.concessionaireModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Relation
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.loginModule.actions.LoginAction
import com.ops.opside.flows.sign_on.concessionaireModule.action.ConcessionaireCrudAction
import com.ops.opside.flows.sign_on.concessionaireModule.model.ConcessionaireCrudInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConcessionaireCrudViewModel @Inject constructor(
    private var mConcessionaireCrudInteractor: ConcessionaireCrudInteractor
): CommonViewModel() {
    private val _action: SingleLiveEvent<ConcessionaireCrudAction> = SingleLiveEvent()
    fun getAction(): LiveData<ConcessionaireCrudAction> = _action

    private val _getRelatesList = MutableLiveData<MutableList<ParticipatingConcessSE>>()
    private val _getMarkets = MutableLiveData<MutableList<MarketSE>>()

    val getRelatesList: LiveData<MutableList<ParticipatingConcessSE>> = _getRelatesList
    val getMarkets: LiveData<MutableList<MarketSE>> = _getMarkets

    fun getRelatesList(idConcessionaire: String){
        disposable.add(
            mConcessionaireCrudInteractor.getRelatesList(idConcessionaire).applySchedulers()
                .subscribe(
                    {
                        _getRelatesList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                        _action.value = ConcessionaireCrudAction.MessageError(it.message.toString())
                    }
                )
        )
    }

    fun deleteRelate(relation: ParticipatingConcessSE){
        disposable.add(
            mConcessionaireCrudInteractor.deleteRelate(relation).applySchedulers()
                .subscribe(
                    {
                        _action.value = ConcessionaireCrudAction.SucessEliminationRelation(relation)
                    },
                    {
                        Log.e("Error", it.toString())
                        _action.value = ConcessionaireCrudAction.MessageError(it.message.toString())
                    }
                )
        )
    }

    fun getMarketList(){
        disposable.add(
            mConcessionaireCrudInteractor.getMarkets().applySchedulers()
                .subscribe(
                    {
                        _getMarkets.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                        _action.value = ConcessionaireCrudAction.MessageError(it.message.toString())
                    }
                )
        )
    }

    fun addMarketToConcess(idMarket: String, idConcessionaire: String){
        disposable.add(
            mConcessionaireCrudInteractor.addMarketToConcess(idMarket, idConcessionaire).applySchedulers()
                .subscribe(
                    {
                        _action.value = ConcessionaireCrudAction.SuccesRelation
                    },
                    {
                        Log.e("Error", it.toString())
                        _action.value = ConcessionaireCrudAction.MessageError(it.message.toString())
                    }
                )
        )
    }
}