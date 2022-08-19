package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.model.TaxCollectionInteractor

class TaxCollectionViewModel: CommonViewModel() {
    private val mTaxCollectionInteractor = TaxCollectionInteractor()
    private val _getConcessionairesFEList = MutableLiveData<MutableList<ConcessionaireFE>>()

    val getConcessionairesFEList: LiveData<MutableList<ConcessionaireFE>> = _getConcessionairesFEList

    fun getConcessionairesFEList(){

    }
}