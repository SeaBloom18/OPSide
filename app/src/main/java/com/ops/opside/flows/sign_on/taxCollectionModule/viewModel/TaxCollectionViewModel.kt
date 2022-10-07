package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.profileModule.model.profileInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.model.PickMarketInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.model.TaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaxCollectionViewModel @Inject constructor(
    private val mTaxCollectionInteractor: TaxCollectionInteractor,
    private val mPickMarketsInteractor: PickMarketInteractor,
    private val mProfileInteractor: profileInteractor
) : CommonViewModel() {


    private val _initTaxCollection = MutableLiveData<Boolean>()
    private val _hasOpenedTaxCollection = MutableLiveData<TaxCollectionSE?>()
    private val _getConcessionairesFEList = MutableLiveData<MutableList<ConcessionaireFE>>()
    private val _getConcessionairesSEList = MutableLiveData<MutableList<ConcessionaireSE>>()
    private val _getParticipatingConcessList =
        MutableLiveData<MutableList<ParticipatingConcessRE>>()
    private val _persistConcessionairesSEList = MutableLiveData<Boolean>()
    private val _persistMarketSE = MutableLiveData<Boolean>()
    private val _participatingConcess = MutableLiveData<Boolean>()
    private val _updateTaxCollection = MutableLiveData<Boolean>()
    private val _createEvent = MutableLiveData<Boolean>()
    private val _revertEvent = MutableLiveData<Boolean>()

    val initTaxCollection: LiveData<Boolean> = _initTaxCollection
    val hasOpenedTaxCollection: LiveData<TaxCollectionSE?> = _hasOpenedTaxCollection
    val getConcessionairesFEList: LiveData<MutableList<ConcessionaireFE>> =
        _getConcessionairesFEList
    val getParticipatingConcessList: LiveData<MutableList<ParticipatingConcessRE>> =
        _getParticipatingConcessList
    val getConcessionairesSEList: LiveData<MutableList<ConcessionaireSE>> =
        _getConcessionairesSEList
    val persistConcessionairesSEList: LiveData<Boolean> = _persistConcessionairesSEList
    val persistMarketSE: LiveData<Boolean> = _persistMarketSE
    val participatingConcess: LiveData<Boolean> = _participatingConcess
    val updateTaxCollection: LiveData<Boolean> = _updateTaxCollection
    val createEvent: LiveData<Boolean> = _createEvent
    val revertEvent: LiveData<Boolean> = _revertEvent



    fun initTaxCollection(taxCollection: TaxCollectionSE){
        disposable.add(
            mTaxCollectionInteractor.createTaxCollection(taxCollection).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
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
                        showProgress.value = false
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
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getParticipatingConcessList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getPersistedParticipatingConcessList(idMarket: String) {
        disposable.add(
            mTaxCollectionInteractor.getPersistedParticipatingConcessList(idMarket).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
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
        participatingConcess: MutableList<ParticipatingConcessRE>
    ) {
        disposable.add(
            mTaxCollectionInteractor.persistConcessionairesSEList(
                idMarket,
                concessionaires,
                participatingConcess
            )
                .applySchedulers()
                .doOnSubscribe { showProgress.value = true }
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
                        showProgress.value = false
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
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
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

    fun isOnLineMode(): Boolean{
        return mPickMarketsInteractor.isOnLineMode()
    }

    fun getCollectorName(): String{
        return mProfileInteractor.getCollectorName()
    }

    fun deleteProfileData(){
        mProfileInteractor.deleteProfileData()
    }

}