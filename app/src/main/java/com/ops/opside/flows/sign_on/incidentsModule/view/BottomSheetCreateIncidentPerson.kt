package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetCreateIncidentBinding
import com.ops.opside.databinding.BottomSheetCreateIncidentPersonBinding
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.BottomSheetCreateIncidentPersonViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetCreateIncidentPerson(private val incident: (IncidentSE) -> Unit = {}): BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetCreateIncidentPersonBinding
    private lateinit var mActivity: MainActivity
    private val mBottomSheetCreateIncidentPersonViewModel: BottomSheetCreateIncidentPersonViewModel by viewModels()
    private lateinit var mConcessionaireList: MutableList<ConcessionaireSE>
    private lateinit var mTaxCollectionList: MutableList<TaxCollectionSE>
    private lateinit var mIncidentList: MutableList<IncidentSE>
    private val mIncidentPersonFE: IncidentPersonFE = IncidentPersonFE()
    @Inject lateinit var preferences: Preferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = BottomSheetCreateIncidentPersonBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            btnCreateIncident.setOnClickListener {
                insertIncident()
            }
            btnClose2.setOnClickListener { dismiss() }
        }
        setUpActivity()
        getLists()
        bindViewModel()
    }

    private fun insertIncident() {
        with(mIncidentPersonFE) {
            val teIncidentName = mBinding.teIncidentName.text.toString().trim()
            val teConcessionaireName = mBinding.teSearch.text.toString().trim()
            val teTaxCollectionName = mBinding.teTaxCollection.text.toString().trim()
            val teIncidentPrice = mBinding.teIncidentPrice.text.toString().trim()
            val dateTimeFormatter = CalendarUtils.getCurrentTimeStamp(FORMAT_SQL_DATE)
            val timeFor = CalendarUtils.getCurrentTimeStamp(FORMAT_TIME)
            if (teIncidentName.isNotEmpty() && teConcessionaireName.isNotEmpty()
                && teTaxCollectionName.isNotEmpty() && teIncidentPrice.isNotEmpty()) {
                incidentName = teIncidentName
                idCollector = preferences.getString(SP_ID).toString()
                reportName = preferences.getString(SP_NAME).toString()
                assignName = teConcessionaireName
                date = dateTimeFormatter
                time = timeFor
                idIncident = "id incident"
                price = teIncidentPrice.toDouble()
                idTaxCollection = teTaxCollectionName
                idConcessionaire = ""
                mBottomSheetCreateIncidentPersonViewModel.funInsertIncident(mIncidentPersonFE)
            } else { toast("debes de llenar todos los valorea") }
        }

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
        mBottomSheetCreateIncidentPersonViewModel.getConcessionairesList()
        mBottomSheetCreateIncidentPersonViewModel.getTaxCollectionList()
        mBottomSheetCreateIncidentPersonViewModel.getIncidentList()
    }
    private fun bindViewModel() {
        mBottomSheetCreateIncidentPersonViewModel.getConcessionairesList.observe(this,
            Observer(this::getConcessionaireList))
        mBottomSheetCreateIncidentPersonViewModel.getTaxCollectionList.observe(this,
            Observer(this::getTaxCollectionList))
        mBottomSheetCreateIncidentPersonViewModel.getIncidentList.observe(this,
            Observer(this::getIncidentList))
    }

    /** Incidents List setUp **/
    private fun getIncidentList(incidentList: MutableList<IncidentSE>) {
        mIncidentList = incidentList
        setUpIncidentList()
    }

    private fun getIncidentListNames(): MutableList<String> {
        return mIncidentList.map { it.incidentName }
            .toMutableList()
    }
    private fun setUpIncidentList() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                getIncidentListNames())
        mBinding.teIncidentPrice.setAdapter(adapter)
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
        //text to show: marketName, date
        return mTaxCollectionList.map { "${it.marketName}, ${it.startDate} to ${it.endDate}" }
            .toMutableList()
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