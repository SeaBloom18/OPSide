package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetCreateIncidentBinding
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.CreateIncidentsViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetCreateMarket(private val incident: (IncidentSE) -> Unit = {}): BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetCreateIncidentBinding
    private lateinit var mActivity: MainActivity
    private val mCreateIncidentsViewModel: CreateIncidentsViewModel by viewModels()
    private lateinit var mConcessionaireList: MutableList<ConcessionaireSE>
    private lateinit var mTaxCollectionList: MutableList<TaxCollectionSE>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = BottomSheetCreateIncidentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            btnCreateIncident.setOnClickListener { toast(getString(R.string.tax_collection_toast_incident_success)) }
            btnClose2.setOnClickListener { dismiss() }
        }
        setUpActivity()
        getLists()
        bindViewModel()
    }

    /** ViewModel SetUp **/

    private fun getLists() {
        mCreateIncidentsViewModel.getConcessionairesList()
        mCreateIncidentsViewModel.getTaxCollectionList()
    }
    private fun bindViewModel() {
        mCreateIncidentsViewModel.getConcessionairesList.observe(this,
            Observer(this::getConcessionaireList))
        mCreateIncidentsViewModel.getTaxCollectionList.observe(this, Observer(this::getTaxCollectionList))
    }

    /** TaxCollector List setUp **/
    private fun getTaxCollectionList(taxCollectionList: MutableList<TaxCollectionSE>) {
        mTaxCollectionList = taxCollectionList
        setUpTaxCollectionList()
    }

    private fun setUpTaxCollectionList() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                getTaxCollectionListNames())
        mBinding.teTaxCollection.setAdapter(adapter)
    }

    private fun getTaxCollectionListNames(): MutableList<String> {
        return mTaxCollectionList.map { it.marketName }.toMutableList()
    }

    /** Concessionaire List setUp**/
    private fun getConcessionaireList(ConcessionaireList: MutableList<ConcessionaireSE>){
        mConcessionaireList = ConcessionaireList
        setUpConcessionaireList()
    }

    private fun setUpConcessionaireList() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                getConcessionaireListNames())
        mBinding.teSearch.setAdapter(adapter)
    }

    private fun getConcessionaireListNames(): MutableList<String> {
        return mConcessionaireList.map { it.name }.toMutableList()
    }

    private fun setUpActivity() {
        mActivity = activity as MainActivity
    }
}