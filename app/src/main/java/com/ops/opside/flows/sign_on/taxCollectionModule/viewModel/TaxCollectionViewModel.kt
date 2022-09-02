package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.TaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaxCollectionViewModel @Inject constructor(
    private val mTaxCollectionInteractor: TaxCollectionInteractor
) : CommonViewModel() {

    private val _getConcessionairesFEList = MutableLiveData<MutableList<ConcessionaireFE>>()
    private val _getConcessionairesSEList = MutableLiveData<MutableList<ConcessionaireSE>>()
    private val _persistConcessionairesSEList = MutableLiveData<Boolean>()
    private val _persistMarketSE = MutableLiveData<Boolean>()

    val getConcessionairesFEList: LiveData<MutableList<ConcessionaireFE>> =
        _getConcessionairesFEList
    val getConcessionairesSEList: LiveData<MutableList<ConcessionaireSE>> =
        _getConcessionairesSEList
    val persistConcessionairesSEList: LiveData<Boolean> = _persistConcessionairesSEList
    val persistMarketSE: LiveData<Boolean> = _persistMarketSE

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

    fun persistConcessionairesSEList(
        idMarket: String,
        concessionaires: MutableList<ConcessionaireFE>
    ) {
        disposable.add(
            mTaxCollectionInteractor.persistConcessionairesSEList(idMarket, concessionaires)
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

}