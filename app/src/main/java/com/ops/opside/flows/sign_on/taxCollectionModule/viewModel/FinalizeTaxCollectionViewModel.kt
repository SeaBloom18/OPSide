package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_off.loginModule.model.LoginInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.FinalizeTaxCollectionAction
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ABSENCE
import com.ops.opside.flows.sign_on.taxCollectionModule.model.FinalizeTaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinalizeTaxCollectionViewModel @Inject constructor(
    private val mFinalizeTaxCollectionInteractor: FinalizeTaxCollectionInteractor,
    private val mLoginInteractor: LoginInteractor
) : CommonViewModel(), BiometricsManager.BiometricListener {

    private val _action: SingleLiveEvent<FinalizeTaxCollectionAction> = SingleLiveEvent()
    fun getAction(): LiveData<FinalizeTaxCollectionAction> = _action

    private var biometricsManager: BiometricsManager? = null

    fun searchIfExistTaxCollection(
        taxCollection: TaxCollectionSE,
        absences: MutableList<ConcessionaireSE>
    ) {
        disposable.add(
            mFinalizeTaxCollectionInteractor.searchIfExistTaxCollection(taxCollection.idFirebase)
                .applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    { exist ->
                        if (exist.not()) {
                            insertTaxCollection(taxCollection, absences)
                        } else {
                            val events =
                                mFinalizeTaxCollectionInteractor.getEventsList(taxCollection.idFirebase)
                            if (events.size > 0) {
                                insertAllEvents(
                                    events,
                                    absences,
                                    taxCollection.idFirebase,
                                    taxCollection.idFirebase,
                                    taxCollection.idMarket
                                )
                            } else {
                                _action.value = FinalizeTaxCollectionAction.SendEmails
                            }
                        }
                    },
                    {
                        showProgress.value = false
                        _action.value =
                            FinalizeTaxCollectionAction.ShowMessageError(it.message.toString())
                    }
                )
        )
    }

    private fun insertTaxCollection(taxCollection: TaxCollectionSE, absences: MutableList<ConcessionaireSE>) {
        disposable.add(
            mFinalizeTaxCollectionInteractor.insertTaxCollection(taxCollection).applySchedulers()
                .subscribe(
                    { id ->
                        val beforeId = taxCollection.idFirebase
                        val result = mFinalizeTaxCollectionInteractor.updateTaxCollectionId(
                            taxCollection,
                            id
                        )
                        if (result.first) {
                            mFinalizeTaxCollectionInteractor
                            insertAllEvents(
                                mFinalizeTaxCollectionInteractor.getEventsList(beforeId),
                                absences,
                                id,
                                beforeId,
                                taxCollection.idMarket
                            )
                        } else {
                            showProgress.value = false
                            _action.value =
                                FinalizeTaxCollectionAction.ShowMessageError(result.second)
                        }
                    },
                    {
                        showProgress.value = false
                        _action.value =
                            FinalizeTaxCollectionAction.ShowMessageError(it.message.toString())
                    }
                )
        )
    }

    private fun insertAllEvents(
        events: MutableList<EventRE>,
        absences: MutableList<ConcessionaireSE>,
        idTaxCollection: String,
        beforeId: String,
        idMarket: String
    ) {
        if (updateIdTaxCollectionEvents(events, idTaxCollection)) {
            val updatedEvents = mFinalizeTaxCollectionInteractor.getEventsList(idTaxCollection)

            absences.map {
                updatedEvents.add(EventRE(
                    id = null,
                    idTaxCollection = idTaxCollection,
                    idConcessionaire = it.idFirebase,
                    nameConcessionaire = it.name,
                    status = ABSENCE,
                    timeStamp = CalendarUtils.getCurrentTimeStamp(FORMAT_TIMESTAMP),
                    amount = 0.0,
                    foreignIdRow = "",
                    idMarket = idMarket
                ))
            }

            disposable.add(
                mFinalizeTaxCollectionInteractor.insertAllEvents(updatedEvents, idTaxCollection)
                    .applySchedulers()
                    .subscribe(
                        {
                            if (it == "Success")
                                _action.value = FinalizeTaxCollectionAction.SendEmails
                            else
                                _action.value =
                                    FinalizeTaxCollectionAction.ShowMessageError(it)
                        }, {
                            showProgress.value = false
                            updateIdTaxCollectionEvents(events, beforeId)
                            _action.value =
                                FinalizeTaxCollectionAction.ShowMessageError(it.message.toString())
                        }
                    )
            )
        } else {
            showProgress.value = false
            _action.value =
                FinalizeTaxCollectionAction.ShowMessageError("No se pudieron actualizar los eventos locales")
        }
    }

    private fun updateIdTaxCollectionEvents(
        events: MutableList<EventRE>,
        idTaxCollection: String
    ): Boolean {
        return try {
            events.map {
                it.idTaxCollection = idTaxCollection
                mFinalizeTaxCollectionInteractor.updateEvent(it)
            }
            true
        } catch (exception: Exception) {
            showProgress.value = false
            _action.value =
                FinalizeTaxCollectionAction.ShowMessageError(exception.message.toString())
            false
        }
    }

    fun closeTaxcollection(taxCollection: TaxCollectionSE) {
        val result = mFinalizeTaxCollectionInteractor.closeTaxCollection(taxCollection)
        if (result.first)
            _action.value = FinalizeTaxCollectionAction.FinalizeCollection
        else
            _action.value = FinalizeTaxCollectionAction.ShowMessageError(result.second)
    }

    fun checkBiometrics(fragment: Fragment) {
        biometricsManager = BiometricsManager(fragment)
        if (isBiometricsActivated())
            biometricsManager?.loadCredentials(this)
        //else
        //_action.value = Promp Contraseña
    }

    private fun isBiometricsActivated() = mLoginInteractor.isBiometricsActivated()

    override fun onSaveFinished() {
        //doNothing
    }

    override fun onCredentialsError(isSaved: Boolean) {
        if (isSaved) {
            //_action.value = Promp Contraseña
        } else {
            mLoginInteractor.setUseBiometrics(false)
        }
    }

    override fun onKeyErrorDeleted() {
        mLoginInteractor.setUseBiometrics(false)
    }

    override fun onCredentialsLoaded(credential: String) {
        _action.value = FinalizeTaxCollectionAction.SendCollection
    }

}