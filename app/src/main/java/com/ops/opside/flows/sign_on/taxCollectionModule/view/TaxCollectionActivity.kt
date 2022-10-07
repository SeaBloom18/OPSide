package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.zxing.integration.android.IntentIntegrator
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.utils.Formaters.orZero
import com.ops.opside.databinding.ActivityTaxCollectionBinding
import com.ops.opside.flows.sign_off.loginModule.view.LoginActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ADDED
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.FLOOR_COLLECTION
import com.ops.opside.flows.sign_on.taxCollectionModule.interfaces.TaxCollectionAux
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.TaxCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaxCollectionActivity : AppCompatActivity(), TaxCollectionAux {

    private val mBinding: ActivityTaxCollectionBinding by lazy {
        ActivityTaxCollectionBinding.inflate(layoutInflater)
    }

    private val mViewModel: TaxCollectionViewModel by viewModels()

    private lateinit var mOpenedTaxCollection: TaxCollectionSE
    private lateinit var mSelectedMarket: MarketFE
    private lateinit var mConcessionairesFEList: MutableList<ConcessionaireFE>
    private var mIsOnLineMode: Boolean = true
    private var mPriceLinearMeter: Float = 0f
    private var mTotalAmount: Double = 0.0

    private lateinit var mConcessionairesMap: MutableMap<String, ConcessionaireSE>
    private lateinit var mParticipatingConcessMap: MutableMap<String, ParticipatingConcessRE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.apply {

            fabRecord.animateOnPress()
            fabRecord.setOnClickListener {
                bsdRecordTaxCollection()
            }

            btnFinalize.setOnClickListener {
                showAlertFinalize()
            }

            btnScan.setOnClickListener {
                initScanner()
            }

            btnScan.setOnLongClickListener {
                initRegisterForeignerConcessionaire()
            }

        }

        bindViewModel()
        bsdPickMarket()
        getPriceLinearMeter()
        setUpPieChart()
    }

    private fun initRegisterForeignerConcessionaire(): Boolean {
        val dialog = BottomSheetForeignerAttendance {

            mConcessionairesMap[it.idFirebase] = it.parseToSE()
            mParticipatingConcessMap[it.idFirebase] = ParticipatingConcessRE(
                idMarket = mSelectedMarket.idFirebase,
                idConcessionaire = it.idFirebase,
                idFirebase = ID.getTemporalId(),
                linearMeters = it.linearMeters,
                lineBusiness = it.lineBusiness
            )
            chargeDay(FLOOR_COLLECTION, it.idFirebase)
        }

        dialog.show(supportFragmentManager, dialog.tag)
        return false
    }

    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))
        mIsOnLineMode = mViewModel.isOnLineMode()
        if (mIsOnLineMode) {
            mViewModel.getConcessionairesFEList.observe(
                this,
                Observer(this::getConcessionairesFEList)
            )
            mViewModel.persistConcessionairesSEList.observe(
                this,
                Observer(this::persistConcessionairesSEList)
            )

            mViewModel.getParticipatingConcessList.observe(
                this,
                Observer(this::getParticipatingConcessList)
            )
        } else {

            mViewModel.getConcessionairesSEList.observe(
                this,
                Observer(this::getConcessionairesSEList)
            )

            mViewModel.getParticipatingConcessList.observe(
                this,
                Observer(this::getPersistedParticipatingConcessList)
            )
        }

        mViewModel.hasOpenedTaxCollection.observe(this, Observer(this::hasOpenedTaxCollection))
        mViewModel.initTaxCollection.observe(this, Observer(this::isInitializedTaxCollection))
        mViewModel.persistMarketSE.observe(this, Observer(this::isAddedMarket))
        mViewModel.updateTaxCollection.observe(this, Observer(this::taxCollectionDataUpdated))
        mViewModel.revertEvent.observe(this, Observer(this::eventWasReverted))
    }

    private fun initTaxCollection() {
        val nameTaxCollector = mViewModel.getCollectorName()

        if (nameTaxCollector == "Desconocido") {
            mViewModel.deleteProfileData()
            launchActivity<LoginActivity> {}
        }

        mOpenedTaxCollection = TaxCollectionSE(
            idFirebase = ID.getTemporalId(),
            idMarket = mSelectedMarket.idFirebase,
            marketName = mSelectedMarket.name,
            totalAmount = 0.0,
            date = CalendarUtils.getCurrentTimeStamp(FORMAT_SQL_DATE),
            startTime = CalendarUtils.getCurrentTimeStamp(FORMAT_TIME),
            endTime = "00:00:00",
            taxCollector = nameTaxCollector
        )

        mViewModel.initTaxCollection(mOpenedTaxCollection)
    }

    private fun hasOpenedTaxCollection(taxCollection: TaxCollectionSE?) {
        if (taxCollection == null) {
            initTaxCollection()
            return
        }

        val dialog = BaseDialog(
            context = this,
            mTitle = getString(R.string.common_atention),
            mDescription = "Tienes una recolección abierta\n¿Deseas continuarla?\n\nNota: No puedes tener 2 recolecciones abiertas del mismo tianguis",
            buttonYesText = getString(R.string.common_accept),
            buttonNoText = getString(R.string.common_cancel),
            yesAction = {
                mOpenedTaxCollection = taxCollection
                mTotalAmount = mOpenedTaxCollection.totalAmount
                updateProgress()

                mViewModel.addMarket(mSelectedMarket)
            },
            noAction = {
                Toast.makeText(
                    this,
                    "Envía la información actual almacenada o eliminala",
                    Toast.LENGTH_LONG
                ).show()
                bsdPickMarket()
            }
        )

        dialog.show()
    }

    private fun isInitializedTaxCollection(isInitialized: Boolean) {
        if (isInitialized)
            mViewModel.addMarket(mSelectedMarket)
    }

    private fun getPriceLinearMeter() {
        mPriceLinearMeter = mViewModel.getPriceLinearMeter()
    }

    private fun populateMaps() {
        mViewModel.getAllConcessionaires()
    }

    private fun showAlertFinalize() {
        val dialog = BaseDialog(
            context = this@TaxCollectionActivity,
            mTitle = getString(R.string.common_atention),
            mDescription = getString(R.string.tax_collection_finalize_collection),
            buttonYesText = getString(R.string.common_accept),
            buttonNoText = "",
            yesAction = { launchFinalizeFragment() }
        )
        dialog.show()
    }

    private fun launchFinalizeFragment() {
        val bundle = Bundle()
        bundle.putString("type", "create")
        bundle.putString("marketId", mSelectedMarket.idFirebase)
        bundle.putString("marketName", mSelectedMarket.name)
        bundle.putParcelableArrayList(
            "absences",
            mConcessionairesMap.filterValues { it.wasPaid.not() }
                .map { it.value } as ArrayList<out Parcelable>
        )

        launchFragment(
            fragment = FinalizeTaxCollectionFragment(),
            fragmentManager = supportFragmentManager,
            containerId = R.id.containerTaxCollection,
            bundle = bundle
        )
    }

    private fun bsdPickMarket() {
        val dialog = BottomSheetPickMarket {
            mSelectedMarket = it
            mBinding.tvMarket.text = mSelectedMarket.name

            mViewModel.getOpenedTaxCollection(mSelectedMarket.idFirebase)
        }

        dialog.isCancelable = false
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun isAddedMarket(isAdded: Boolean) {
        if (isAdded) {
            if (mIsOnLineMode)
                mViewModel.getConcessionairesFEList()
            else
                populateMaps()

        } else {
            showError("Hubo un error al guardar la información del Tianguis seleccionado")
        }
    }

    private fun setUpPieChart() {
        mBinding.chartTaxMoney.apply {
            progressMax = 100f
            setProgressWithAnimation(0f, 4000) // =1s

            progressBarColor = Color.BLACK
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = getColor(R.color.secondaryColor)
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            backgroundProgressBarColor = Color.GRAY
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = getColor(R.color.primaryLightColor)
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            progressBarWidth = 28f
            backgroundProgressBarWidth = 30f
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }

    private fun updateProgress() {
        mBinding.chartTaxMoney.setProgressWithAnimation((mTotalAmount).toFloat(), 4000)
        mBinding.tvTotalAmount.text = "$ $mTotalAmount"
    }

    private fun getPersistedParticipatingConcessList(participatingConcess: MutableList<ParticipatingConcessRE>) {
        mParticipatingConcessMap = mutableMapOf()
        participatingConcess.map {
            mParticipatingConcessMap.put(it.idConcessionaire, it)
        }
    }

    private fun getParticipatingConcessList(participatingConcess: MutableList<ParticipatingConcessRE>) {
        mParticipatingConcessMap = mutableMapOf()
        participatingConcess.map {
            mParticipatingConcessMap.put(it.idConcessionaire, it)
        }

        mViewModel.persistConcessionairesSEList(
            mSelectedMarket.idFirebase,
            mConcessionairesFEList,
            participatingConcess
        )

    }

    private fun getConcessionairesFEList(concessionairesList: MutableList<ConcessionaireFE>) {
        mConcessionairesFEList = concessionairesList

        mConcessionairesMap = mutableMapOf()
        concessionairesList.map {
            mConcessionairesMap.put(it.idFirebase, it.parseToSE())
        }

        mViewModel.getParticipatingConcessList(mSelectedMarket.idFirebase)
    }

    private fun getConcessionairesSEList(concessionairesList: MutableList<ConcessionaireSE>) {
        mConcessionairesMap = mutableMapOf()
        concessionairesList.map {
            mConcessionairesMap.put(it.idFirebase, it)
        }

        mViewModel.getPersistedParticipatingConcessList(mSelectedMarket.idFirebase)
    }

    private fun persistConcessionairesSEList(wasAdded: Boolean) {
        if (wasAdded) {
            mViewModel.getAllConcessionaires()
        } else {
            showError("Hubo un error al guardar el listado de concesionarios")
        }
    }

    private fun bsdRecordTaxCollection() {
        val dialog = BottomSheetRecordTaxCollection(mOpenedTaxCollection.idFirebase)
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(false)
        integrator.setPrompt("")
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        if (result.contents == null) {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            return
        }

        setConcessionaireData(result.contents)
    }

    private fun setConcessionaireData(idConcessionaire: String) {
        if (mConcessionairesMap.containsKey(idConcessionaire).not()) {
            mBinding.etStatus.setText("- Concesionario no registrado")
            mBinding.etStatus.setTextColor(getColor(R.color.warning))
            return
        }

        if (mParticipatingConcessMap.containsKey(idConcessionaire).not()) {
            val alert = BaseDialog(
                this,
                getString(R.string.common_atention),
                "El usuario no está dado de alta en este tianguis.\n¿Deseas agregarlo?",
                getString(R.string.common_accept),
                getString(R.string.common_cancel),
                {

                    val dialog = BottomSheetRelateConcessMarket(
                        mConcessionairesMap[idConcessionaire]!!, mSelectedMarket.parseToSE()
                    ) {

                        var status = ""
                        if (it.first) {
                            status = ADDED

                            createNewEvent(
                                status = status,
                                foreignIdRow = it.second.idFirebase,
                                idConcessionaire = idConcessionaire,
                                nameConcessionaire = mConcessionairesMap[idConcessionaire]?.name
                                    ?: "",
                                amount = 0.0
                            )

                        } else {
                            status = "No se pudo agregar al tianguis"
                        }

                        mParticipatingConcessMap[idConcessionaire] = it.second
                        chargeDay(status, idConcessionaire)
                    }

                    dialog.show(supportFragmentManager, dialog.tag)
                }
            )

            alert.show()
            return
        }

        chargeDay(FLOOR_COLLECTION, idConcessionaire)
    }

    private fun createNewEvent(
        status: String,
        foreignIdRow: String = "",
        idConcessionaire: String,
        nameConcessionaire: String,
        amount: Double
    ) {
        val event = EventRE(
            id = null,
            idTaxCollection = mOpenedTaxCollection.idFirebase,
            idConcessionaire = idConcessionaire,
            nameConcessionaire = nameConcessionaire,
            status = status,
            amount = amount,
            timeStamp = CalendarUtils.getCurrentTimeStamp(FORMAT_TIMESTAMP),
            foreignIdRow = foreignIdRow
        )

        mViewModel.createEvent(event)
    }

    private fun setLabelTexts(
        name: String,
        linearMeters: String,
        lineBusiness: String,
        amount: String,
        status: String,
        @ColorRes color: Int = getColor(R.color.secondaryColor)
    ) {
        with(mBinding) {
            etDealerName.setText(name)
            etLinearMeters.setText(linearMeters)
            etLineOfBusiness.setText(lineBusiness)
            etTotalAmount.setText(amount)
            etStatus.setText(status)
            etStatus.setTextColor(color)
        }
    }

    private fun chargeDay(status: String, idConcessionaire: String) {
        val concessionaire = mConcessionairesMap[idConcessionaire]
        val participating = mParticipatingConcessMap[idConcessionaire]

        if (concessionaire != null && participating != null) {

            try {
                val amount = (participating.linearMeters * mPriceLinearMeter).orZero()
                mTotalAmount += amount

                setLabelTexts(
                    name = concessionaire.name,
                    linearMeters = participating.linearMeters.toString(),
                    lineBusiness = participating.lineBusiness,
                    amount = amount.toString(),
                    status
                )

                updateProgress()

                mOpenedTaxCollection.totalAmount = mTotalAmount
                mViewModel.updateTaxCollection(mOpenedTaxCollection)

                createNewEvent(
                    status = FLOOR_COLLECTION,
                    foreignIdRow = "",
                    idConcessionaire = idConcessionaire,
                    nameConcessionaire = mConcessionairesMap[idConcessionaire]?.name ?: "",
                    amount = amount
                )

                mConcessionairesMap[idConcessionaire]?.wasPaid = true
            } catch (e: Exception) {

            }
        }
    }


    private fun taxCollectionDataUpdated(itWasUpdate: Boolean) {
        if (itWasUpdate.not()) {
            val error = "No se pudo guardar la información capturada"
            showError(error)
        }
    }

    private fun eventWasReverted(itWas: Boolean) {
        if (itWas.not()) {
            Toast.makeText(this, "No se pudo revertir el evento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun revertCollection(event: EventRE) {
        val concessionaire = mConcessionairesMap[event.idConcessionaire]
        val participating = mParticipatingConcessMap[event.idConcessionaire]

        if (concessionaire != null && participating != null) {
            val amount = (participating.linearMeters * mPriceLinearMeter).orZero()

            mTotalAmount -= amount

            updateProgress()

            mOpenedTaxCollection.totalAmount = mTotalAmount
            mViewModel.updateTaxCollection(mOpenedTaxCollection)

            mConcessionairesMap[event.idConcessionaire]?.wasPaid = false
        }
    }

    /*
    TaxCollectionAuxListener
     */

    override fun hideButtons() {
        mBinding.btnFinalize.visibility = View.GONE
        mBinding.btnScan.visibility = View.GONE
        mBinding.fabRecord.visibility = View.GONE
    }

    override fun showButtons() {
        mBinding.btnFinalize.visibility = View.VISIBLE
        mBinding.btnScan.visibility = View.VISIBLE
        mBinding.fabRecord.visibility = View.VISIBLE
    }

    override fun revertEvent(event: EventRE) {
        when (event.status) {
            ADDED -> {
                mViewModel.revertRelatedConcess(event.foreignIdRow)
            }
            FLOOR_COLLECTION -> {
                revertCollection(event)
            }
            else -> {
                Toast.makeText(this, "Evento desconocido", Toast.LENGTH_SHORT).show()
            }
        }

        setLabelTexts(
            name = "",
            linearMeters = "",
            lineBusiness = "",
            amount = "",
            status = ""
        )
    }
}