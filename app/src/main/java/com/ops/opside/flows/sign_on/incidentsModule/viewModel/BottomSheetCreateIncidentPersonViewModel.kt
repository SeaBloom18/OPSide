package com.ops.opside.flows.sign_on.incidentsModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.incidentsModule.model.BottomSheetCreateIncidentPersonInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by davidgonzalez on 22/01/23
 */
@HiltViewModel
class BottomSheetCreateIncidentPersonViewModel @Inject constructor(
    private val mBottomSheetCreateIncidentPersonInteractor: BottomSheetCreateIncidentPersonInteractor): CommonViewModel() {

    private val mGetConcessionairesList = MutableLiveData<MutableList<ConcessionaireSE>>()
    val getConcessionairesList: LiveData<MutableList<ConcessionaireSE>> = mGetConcessionairesList

    private val mGetTaxCollectionList = MutableLiveData<MutableList<TaxCollectionSE>>()
    val getTaxCollectionList: LiveData<MutableList<TaxCollectionSE>> = mGetTaxCollectionList

    private val mGetIncidentList = MutableLiveData<MutableList<IncidentSE>>()
    val getIncidentList: LiveData<MutableList<IncidentSE>> = mGetIncidentList

    val getMarketList = MutableLiveData<MutableMap<String, String>>()

    fun getMarketList(){
        disposable.add(
            mBottomSheetCreateIncidentPersonInteractor.getMarkets().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        getMarketList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
    fun getConcessByMarketList(market: String){
        disposable.add(
            mBottomSheetCreateIncidentPersonInteractor.getConcessByMarket(market).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        mGetConcessionairesList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun getTaxCollectionList() {
        disposable.add(
            mBottomSheetCreateIncidentPersonInteractor.getTaxCollections().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        mGetTaxCollectionList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }

    fun funInsertIncidentPerson(incidentPersonFE: IncidentPersonFE) {
        disposable.add(
            mBottomSheetCreateIncidentPersonInteractor.insertIncidentPerson(incidentPersonFE)
                .applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        //_registerConcessionaire.value = it
                        showProgress.value = false
                        //_action.value = RegistrationAction.ShowMessageSuccess
                    },
                    {
                        //_registerConcessionaire.value = false
                        Log.e("Error", it.toString())
                        showProgress.value = false
                        //_action.value = RegistrationAction.ShowMessageError
                    }
                )
        )
    }

    fun getIncidentList() {
        disposable.add(
            mBottomSheetCreateIncidentPersonInteractor.getIncidentList().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        mGetIncidentList.value = it
                    },
                    {
                        showProgress.value = false
                        Log.e("Error", it.toString())
                    }
                )
        )
    }
}