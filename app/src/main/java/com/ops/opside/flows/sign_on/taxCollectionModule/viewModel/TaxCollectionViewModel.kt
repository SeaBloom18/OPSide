package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.profileModule.model.ProfileInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.TaxCollectionAction
import com.ops.opside.flows.sign_on.taxCollectionModule.model.PickMarketInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.model.RecordTaxCollectionInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.model.TaxCollectionInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaxCollectionViewModel @Inject constructor(
    private val mTaxCollectionInteractor: TaxCollectionInteractor,
    private val mPickMarketsInteractor: PickMarketInteractor,
    private val mProfileInteractor: ProfileInteractor,
    private val mRecordEventInteractor: RecordTaxCollectionInteractor
) : CommonViewModel() {
    private val _action: SingleLiveEvent<TaxCollectionAction> = SingleLiveEvent()
    fun getAction(): LiveData<TaxCollectionAction> = _action

    private val _initTaxCollection = MutableLiveData<Boolean>()
    private val _hasOpenedTaxCollection = MutableLiveData<TaxCollectionSE?>()
    private val _getConcessionairesFEList = MutableLiveData<MutableList<ConcessionaireFE>>()
    private val _getConcessionairesSEList = MutableLiveData<MutableList<ConcessionaireSE>>()
    private val _getParticipatingConcessList =
        MutableLiveData<MutableList<ParticipatingConcessSE>>()
    private val _persistConcessionairesSEList = MutableLiveData<Boolean>()
    private val _persistMarketSE = MutableLiveData<Boolean>()
    private val _participatingConcess = MutableLiveData<Boolean>()
    private val _updateTaxCollection = MutableLiveData<Boolean>()
    private val _createEvent = MutableLiveData<Boolean>()
    private val _revertEvent = MutableLiveData<Boolean>()
    private val _getAbsences = MutableLiveData<MutableMap<String,Int>>()

    val initTaxCollection: LiveData<Boolean> = _initTaxCollection
    val hasOpenedTaxCollection: LiveData<TaxCollectionSE?> = _hasOpenedTaxCollection
    val getConcessionairesFEList: LiveData<MutableList<ConcessionaireFE>> =
        _getConcessionairesFEList
    val getParticipatingConcessList: LiveData<MutableList<ParticipatingConcessSE>> =
        _getParticipatingConcessList
    val getConcessionairesSEList: LiveData<MutableList<ConcessionaireSE>> =
        _getConcessionairesSEList
    val persistConcessionairesSEList: LiveData<Boolean> = _persistConcessionairesSEList
    val persistMarketSE: LiveData<Boolean> = _persistMarketSE
    val participatingConcess: LiveData<Boolean> = _participatingConcess
    val updateTaxCollection: LiveData<Boolean> = _updateTaxCollection
    val revertEvent: LiveData<Boolean> = _revertEvent
    val getAbsences: LiveData<MutableMap<String,Int>> = _getAbsences



    fun initTaxCollection(taxCollection: TaxCollectionSE){
        disposable.add(
            mTaxCollectionInteractor.createTaxCollection(taxCollection).applySchedulers()
                .subscribe(
                    {
                        _initTaxCollection.value = true
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _initTaxCollection.value = false
                    }
                )
        )
    }

    fun deleteTaxCollection(taxCollection: TaxCollectionSE){
        val result = mTaxCollectionInteractor.deleteTaxCollection(taxCollection)
        if (result.first){
            _action.value = TaxCollectionAction.ResetActivity
        } else{
            Log.d("Error", result.second)
            _action.value = TaxCollectionAction.ShowMessageError(result.second)
        }
    }

    fun getOpenedTaxCollection(idMarket: String){
        disposable.add(
            mTaxCollectionInteractor.getOpenedTaxCollection(idMarket).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _hasOpenedTaxCollection.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _hasOpenedTaxCollection.value = null
                    }
                )
        )
    }

    fun getConcessionairesFEList() {
        disposable.add(
            mTaxCollectionInteractor.getConcessionairesFEList().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _getConcessionairesFEList.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                    }
                )
        )
    }

    fun getParticipatingConcessList(idMarket: String) {
        disposable.add(
            mTaxCollectionInteractor.getParticipatingConcessList(idMarket).applySchedulers()
                .subscribe(
                    {
                        _getParticipatingConcessList.value = it
                        getAbsences(idMarket)
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    private fun getAbsences(idMarket: String){
        disposable.add(
            mTaxCollectionInteractor.getAbsences(idMarket).applySchedulers()
                .subscribe(
                    {
                        _getAbsences.value = it
                    },
                    {
                        showProgress.value = false
                        _action.value = TaxCollectionAction.ShowMessageError(it.message.toString())
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getPersistedParticipatingConcessList(idMarket: String) {
        disposable.add(
            mTaxCollectionInteractor.getPersistedParticipatingConcessList(idMarket).applySchedulers()
                .subscribe(
                    {
                        _getParticipatingConcessList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun persistConcessionairesSEList(
        idMarket: String,
        concessionaires: MutableList<ConcessionaireFE>,
        participatingConcess: MutableList<ParticipatingConcessSE>
    ) {
        disposable.add(
            mTaxCollectionInteractor.persistConcessionairesSEList(
                idMarket,
                concessionaires,
                participatingConcess
            )
                .applySchedulers()
                .subscribe(
                    {
                        showProgress.value = false
                        _persistConcessionairesSEList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _persistConcessionairesSEList.value = false
                    }
                )
        )
    }

    fun getAllConcessionaires() {
        disposable.add(
            mTaxCollectionInteractor.getAllConcessionaires().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        _getConcessionairesSEList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getPriceLinearMeter(): Float {
        return mTaxCollectionInteractor.getPriceLinearMeter()
    }

    fun addMarket(market: MarketFE) {
        disposable.add(
            mTaxCollectionInteractor.persistMarket(market).applySchedulers()
                .subscribe(
                    {
                        _persistMarketSE.value = true
                    },
                    {
                        showProgress.value = false
                        _persistMarketSE.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun updateTaxCollection(taxCollection: TaxCollectionSE){
        disposable.add(
            mTaxCollectionInteractor.updateTaxCollection(taxCollection).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _updateTaxCollection.value = true
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _updateTaxCollection.value = false
                    }
                )
        )
    }

    fun createEvent(event: EventRE){
        disposable.add(
            mTaxCollectionInteractor.createEvent(event).applySchedulers()
                .subscribe(
                    {
                        _createEvent.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                        _createEvent.value = false
                    }
                )
        )
    }

    fun getAllEvents(idTaxCollection: String) =
            mRecordEventInteractor.getEventsList(idTaxCollection)


    fun hasEvents(idTaxCollection: String) = mRecordEventInteractor.hasEvents(idTaxCollection)

    fun revertRelatedConcess(idFirebase: String){
        disposable.add(
            mTaxCollectionInteractor.revertRelatedConcess(idFirebase).applySchedulers()
                .subscribe(
                    {
                        _revertEvent.value = it
                    },
                    {
                        Log.e("Error", it.toString())
                        _revertEvent.value = false
                    }
                )
        )
    }

    fun sendReceipt(email: TaxCollectionActivity.Email){
        _action.value = TaxCollectionAction.SendReceipt(email)
    }

    fun isOnLineMode(): Boolean{
        return mPickMarketsInteractor.isOnLineMode()
    }

    fun getCollectorName() = mProfileInteractor.getCollectorName()
    fun getCollectorId() = mProfileInteractor.getCollectorId()


    fun deleteProfileData(){
        mProfileInteractor.deleteProfileData()
    }

}