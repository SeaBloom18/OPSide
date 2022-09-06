package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.PickMarketInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.model.TaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaxCollectionViewModel @Inject constructor(
    private val mTaxCollectionInteractor: TaxCollectionInteractor,
    private val mPickMarketsInteractor: PickMarketInteractor
) : CommonViewModel() {

    private val _getConcessionairesFEList = MutableLiveData<MutableList<ConcessionaireFE>>()
    private val _getConcessionairesSEList = MutableLiveData<MutableList<ConcessionaireSE>>()
    private val _getParticipatingConcessList =
        MutableLiveData<MutableList<ParticipatingConcessRE>>()
    private val _persistConcessionairesSEList = MutableLiveData<Boolean>()
    private val _persistMarketSE = MutableLiveData<Boolean>()
    private val _participatingConcess = MutableLiveData<Boolean>()

    val getConcessionairesFEList: LiveData<MutableList<ConcessionaireFE>> =
        _getConcessionairesFEList
    val getParticipatingConcessList: LiveData<MutableList<ParticipatingConcessRE>> =
        _getParticipatingConcessList
    val getConcessionairesSEList: LiveData<MutableList<ConcessionaireSE>> =
        _getConcessionairesSEList
    val persistConcessionairesSEList: LiveData<Boolean> = _persistConcessionairesSEList
    val persistMarketSE: LiveData<Boolean> = _persistMarketSE
    val participatingConcess: LiveData<Boolean> = _participatingConcess

    fun getConcessionairesFEList(idMarket: String) {
        disposable.add(
            mTaxCollectionInteractor.getConcessionairesFEList(idMarket).applySchedulers()
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

    fun addConcessionaireToMarket(participatingConcess: ParticipatingConcessRE){
        disposable.add(
            mTaxCollectionInteractor.addConcessionaireToMarket(participatingConcess).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _participatingConcess.value = true
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                        _participatingConcess.value = false
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

    fun isOnLineMode(): Boolean{
        return mPickMarketsInteractor.isOnLineMode()
    }

}