package com.ops.opside.flows.sign_on.incidentsModule.view

import  android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetCreateIncidentPersonBinding
import com.ops.opside.flows.sign_on.incidentsModule.actions.CreateIncidentAction
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.BottomSheetCreateIncidentPersonViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.marketModule.actions.MarketAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetCreateIncidentPerson(private val incident: (IncidentSE) -> Unit = {})
    : BaseBottomSheetFragment() {

    @Inject lateinit var preferences: Preferences
    private lateinit var mBinding: BottomSheetCreateIncidentPersonBinding
    private val mActivity: MainActivity by lazy { activity as MainActivity }
    private lateinit var mConcessionaireList: MutableMap<String, String>
    private lateinit var mTaxCollectionList: MutableList<TaxCollectionSE>
    private lateinit var mIncidentList: MutableMap<String, String>
    private lateinit var mMarketList: MutableMap<String, String>
    private val mBottomSheetCreateIncidentPersonViewModel: BottomSheetCreateIncidentPersonViewModel by viewModels()
    private val mIncidentPersonFE: IncidentPersonFE = IncidentPersonFE()
    private val monthList: MutableList<String> = mutableListOf("Enero", "Febrero")
    private val yearsList: MutableList<String> = mutableListOf("2022", "2023")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = BottomSheetCreateIncidentPersonBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            teSelectMarket.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int,
                                               count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    teSelectConcessionaire.isEnabled = true
                    tilSelectConcessionaire.isEnabled = true
                    mBottomSheetCreateIncidentPersonViewModel.getConcessByMarketList(
                        mMarketList[teSelectMarket.text.toString()].orEmpty())
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            val monthAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, monthList)
            val yearsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, yearsList)

            btnCreateIncidentPerson.setOnClickListener { insertIncident() }
            btnDismiss.setOnClickListener { dismiss() }
            teSelectMonth.setAdapter(monthAdapter)
            teSelectYear.setAdapter(yearsAdapter)
        }

        /** Method call's **/
        getLists()
        bindViewModel()

    }

    /** Methods **/
    private fun insertIncident() {
        with(mIncidentPersonFE) {
            idCollector = preferences.getString(SP_ID).toString()
            reportName = preferences.getString(SP_NAME).toString()
            assignName = mBinding.teSelectConcessionaire.text.toString().trim()
            date = CalendarUtils.getCurrentTimeStamp(FORMAT_TIMESTAMP)
            price = 12.3
            idIncident = mIncidentList[mBinding.teSelectIssueTrack.text.toString()].toString()
            idConcessionaire = mConcessionaireList[mBinding.teSelectConcessionaire.text.toString()].toString()
            idTaxCollection = mBinding.teTaxCollection.text.toString().trim()
        }
        mBottomSheetCreateIncidentPersonViewModel.funInsertIncidentPerson(mIncidentPersonFE)
        /*
        * assignName = Nombre de la persona multada. (done)
        date = Fecha de cuando se asignó la multa (Debe cambiarse a timeStamp, actualmente es una string).
        idCollector = el id del recolector que está generando la multa. (done)
        idIncident = id de firebase del documento que representa la multa, en la tabla de incidents.
        idTaxCollection = id de firebase de la recolección en la que se generó la multa.
        IncidentName = Nombre de la incidencia, este dato también se saca de la tabla de incidents. (done)
        price = monto total a pagar, este dato también se saca de la tabla de incidencias. ()
        reportName = Nombre del recolector que generó la multa.
        time = desaparecerá, ya que este dato se encontrará en el dato Date al convertirse en timeStamp.
        También se añadirá el idConcessionaire, que será el id de firebase del concesionario al que se le asigna la multa.
        * */
    }

    /** ViewModel SetUp **/
    private fun getLists() {
        mBottomSheetCreateIncidentPersonViewModel.getTaxCollectionList()
        mBottomSheetCreateIncidentPersonViewModel.getIncidentList()
        mBottomSheetCreateIncidentPersonViewModel.getMarketList()
    }
    private fun bindViewModel() {
        mBottomSheetCreateIncidentPersonViewModel.getShowProgress().observe(mActivity,
            Observer(mActivity::showLoading))
        mBottomSheetCreateIncidentPersonViewModel.getConcessionairesList.observe(this,
            Observer(this::getConcessionaireList))
        mBottomSheetCreateIncidentPersonViewModel.getTaxCollectionList.observe(this,
            Observer(this::getTaxCollectionList))
        mBottomSheetCreateIncidentPersonViewModel.getIncidentList.observe(this,
            Observer(this::getIncidentList))
        mBottomSheetCreateIncidentPersonViewModel.getMarketList.observe(this,
            Observer(this::getMarketList))
        mBottomSheetCreateIncidentPersonViewModel.getAction().observe(mActivity, Observer(this::handleAction))

    }

    private fun handleAction(action: CreateIncidentAction) {
        when(action) {
            is CreateIncidentAction.ShowMessageSuccess -> {
                toast(getString(R.string.bs_assign_incident_handle_success))
            }
            is CreateIncidentAction.ShowMessageError -> {
                toast(getString(R.string.bs_assign_incident_handle_error))
            }
        }
    }

    /** Markets List setUp **/
    private fun getMarketList(marketList: MutableMap<String, String>) {
        mMarketList = marketList
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                mMarketList.map { it.key }.toMutableList())
        mBinding.teSelectMarket.setAdapter(adapter)    }
    
    /** Incidents List setUp **/
    private fun getIncidentList(incidentList: MutableMap<String, String>) {
        mIncidentList = incidentList
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                mIncidentList.map { it.key }.toMutableList())
        mBinding.teSelectIssueTrack.setAdapter(adapter)
    }

    /** TaxCollector List setUp **/
    private fun getTaxCollectionList(taxCollectionList: MutableList<TaxCollectionSE>) {
        mTaxCollectionList = taxCollectionList
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                mTaxCollectionList.map { "${it.marketName}, ${it.startDate} to ${it.endDate}" })
        mBinding.teTaxCollection.setAdapter(adapter)    }

    /** Concessionaire List setUp **/
    private fun getConcessionaireList(ConcessionaireList: MutableMap<String, String>){
        mConcessionaireList = ConcessionaireList
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                mConcessionaireList.map { it.key }.toMutableList())
        mBinding.teSelectConcessionaire.setAdapter(adapter)
    }
}