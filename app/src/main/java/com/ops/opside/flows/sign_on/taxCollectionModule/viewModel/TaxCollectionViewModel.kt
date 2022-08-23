package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.TaxCollectionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaxCollectionViewModel @Inject constructor(
    private val mTaxCollectionInteractor: TaxCollectionInteractor
): CommonViewModel() {

    private val _getConcessionairesFEList = MutableLiveData<MutableList<ConcessionaireFE>>()

    val getConcessionairesFEList: LiveData<MutableList<ConcessionaireFE>> = _getConcessionairesFEList

    fun getConcessionairesFEList(){

    }

}