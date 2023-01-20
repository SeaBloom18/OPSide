package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.lifecycle.Observer
import com.google.zxing.integration.android.IntentIntegrator
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.utils.Formaters.formatCurrency
import com.ops.opside.common.utils.Formaters.orZero
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityTaxCollectionBinding
import com.ops.opside.flows.sign_off.loginModule.view.LoginActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.TaxCollectionAction
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ADDED
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.FLOOR_COLLECTION
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.EmailObject
import com.ops.opside.flows.sign_on.taxCollectionModule.interfaces.TaxCollectionAux
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.TaxCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaxCollectionActivity : BaseActivity(), TaxCollectionAux {

    private val mBinding: ActivityTaxCollectionBinding by lazy {
        ActivityTaxCollectionBinding.inflate(layoutInflater)
    }

    private val mViewModel: TaxCollectionViewModel by viewModels()

    private lateinit var mOpenedTaxCollection: TaxCollectionSE
    private lateinit var mSelectedMarket: MarketFE
    private lateinit var mConcessionairesFEList: MutableList<ConcessionaireFE>
    private var mCollectorName: String = "Desconocido"
    private var mIsOnLineMode: Boolean = true
    private var mPriceLinearMeter: Float = 0f
    private var mTotalAmount: Double = 0.0

    private lateinit var mConcessionairesMap: MutableMap<String, ConcessionaireSE>
    private lateinit var mParticipatingConcessMap: MutableMap<String, ParticipatingConcessSE>
    private lateinit var mAbsencesMap: MutableMap<String, Int>

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

            btnClose.setOnClickListener { onBackPressed() }
        }

        bindViewModel()
        bsdPickMarket()
        getPriceLinearMeter()
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
        mViewModel.getAbsences.observe(this, Observer(this::getAbsences))
        mViewModel.getAction().observe(this, Observer(this::handleAction))
    }

    private fun handleAction(action: TaxCollectionAction) {
        when (action) {
            is TaxCollectionAction.SendReceipt -> sendReceipt(action.email)
            is TaxCollectionAction.ResetActivity -> resetActivity()
            is TaxCollectionAction.ShowMessageError -> showError(action.error)
            is TaxCollectionAction.SetCollectorName -> mCollectorName = action.collectorName
        }
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

    private fun initRegisterForeignerConcessionaire(): Boolean {
        val dialog = BottomSheetForeignerAttendance {
            if (mConcessionairesMap.containsKey(it.idFirebase).not())
                mConcessionairesMap[it.idFirebase] = it.parseToSE()

            if (mConcessionairesMap[it.idFirebase]!!.wasPaid) {
                showError(getString(R.string.tax_collection_2_times))
            } else {
                if (it.isForeigner) {
                    mParticipatingConcessMap[it.idFirebase] = ParticipatingConcessSE(
                        idMarket = mSelectedMarket.idFirebase,
                        idConcessionaire = it.idFirebase,
                        idFirebase = ID.getTemporalId(),
                        linearMeters = it.linearMeters,
                        lineBusiness = it.lineBusiness,
                        marketName = mSelectedMarket.name
                    )

                    chargeDay(FLOOR_COLLECTION, it.idFirebase)
                } else {
                    setConcessionaireData(it.idFirebase)
                }
            }
        }

        dialog.show(supportFragmentManager, dialog.tag)
        return false
    }

    private fun initTaxCollection() {
        mOpenedTaxCollection = TaxCollectionSE(
            idFirebase = ID.getTemporalId(),
            idMarket = mSelectedMarket.idFirebase,
            idTaxCollector = mViewModel.getCollectorId().orEmpty(),
            marketName = mSelectedMarket.name,
            totalAmount = 0.0,
            startDate = CalendarUtils.getCurrentTimeStamp(FORMAT_SQL_DATE),
            endDate = "01-01-2000",
            startTime = CalendarUtils.getCurrentTimeStamp(FORMAT_TIME),
            endTime = "00:00:00",
            taxCollector = mCollectorName,
        )

        setUpPieChart()

        mViewModel.initTaxCollection(mOpenedTaxCollection)
    }

    private fun hasOpenedTaxCollection(taxCollection: TaxCollectionSE?) {
        mViewModel.getCollectorName()?.let { mCollectorName = it }

        if (mCollectorName == "Desconocido") {
            mViewModel.deleteProfileData()
            launchActivity<LoginActivity> {}
        }

        if (taxCollection == null) {
            initTaxCollection()
            return
        }

        val dialog = BaseDialog(
            imageResource = R.drawable.ic_ops_warning,
            context = this,
            mTitle = getString(R.string.common_atention),
            mDescription = getString(R.string.tax_collection_opened_collection),
            buttonYesText = getString(R.string.common_accept),
            buttonNoText = getString(R.string.common_cancel),
            yesAction = {
                setUpPieChart()
                mOpenedTaxCollection = taxCollection
                mTotalAmount = mOpenedTaxCollection.totalAmount
                updateProgress()
                mViewModel.addMarket(mSelectedMarket)
            },
            noAction = {
                toast(getString(R.string.tax_collection_send_the_info))
                bsdPickMarket()
            }
        )

        dialog.show()
    }

    private fun getAbsences(absences: MutableMap<String,Int>){
        mAbsencesMap = absences
    }

    private fun getAllEvents(events: MutableList<EventRE>) {
        events.filter {
            it.status == FLOOR_COLLECTION
        }.map {
            mConcessionairesMap[it.idConcessionaire]?.wasPaid = true
        }
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

    private fun setUpPieChart() {
        mBinding.chartTaxMoney.apply {
            progressMax = mSelectedMarket.marketMeters.toFloat()
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
        mBinding.chartTaxMoney.setProgressWithAnimation(
            (mTotalAmount / mPriceLinearMeter).toFloat(),
            3000
        )
        mBinding.tvTotalAmount.text = mTotalAmount.formatCurrency()
    }

    private fun getPersistedParticipatingConcessList(participatingConcess: MutableList<ParticipatingConcessSE>) {
        mParticipatingConcessMap = mutableMapOf()
        participatingConcess.map {
            mParticipatingConcessMap.put(it.idConcessionaire, it)
        }
    }

    private fun getParticipatingConcessList(participatingConcess: MutableList<ParticipatingConcessSE>) {
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

        getAllEvents(mViewModel.getAllEvents(mOpenedTaxCollection.idFirebase))
        mViewModel.getParticipatingConcessList(mSelectedMarket.idFirebase)
    }

    private fun getConcessionairesSEList(concessionairesList: MutableList<ConcessionaireSE>) {
        mConcessionairesMap = mutableMapOf()
        concessionairesList.map {
            mConcessionairesMap.put(it.idFirebase, it)
        }

        getAllEvents(mViewModel.getAllEvents(mOpenedTaxCollection.idFirebase))
        mViewModel.getPersistedParticipatingConcessList(mSelectedMarket.idFirebase)
    }

    private fun persistConcessionairesSEList(wasAdded: Boolean) {
        if (wasAdded.not()) {
            showError(getString(R.string.tax_collection_error_persisting_concess))
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
            toast(getString(R.string.common_cancelled))
            return
        }

        val concess = mConcessionairesMap[result.contents]

        if (concess == null) {
            showError(getString(R.string.tax_collection_concess_not_found))
            return
        }

        if (concess.wasPaid) {
            showError(getString(R.string.tax_collection_2_times))
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
                context = this,
                imageResource = R.drawable.ic_store,
                mTitle = getString(R.string.common_atention),
                mDescription = getString(R.string.tax_collection_wish_add_concess),
                buttonYesText = getString(R.string.common_accept),
                buttonNoText = getString(R.string.common_cancel),
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
                                nameConcessionaire = mConcessionairesMap[idConcessionaire]?.name.orEmpty(),
                                amount = 0.0
                            )

                        } else {
                            status = getString(R.string.tax_collection_cant_add_market)
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
            idMarket = mOpenedTaxCollection.idMarket,
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
        absences: Int,
        @ColorRes color: Int = getColor(R.color.secondaryColor)
    ) {
        with(mBinding) {
            etDealerName.setText(name)
            etLinearMeters.setText(linearMeters)
            etLineOfBusiness.setText(lineBusiness)
            etTotalAmount.setText(amount)
            etStatus.setText(status)
            etStatus.setTextColor(color)
            etAbsences.setText(absences.orZero().toString())
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
                    status,
                    mAbsencesMap[concessionaire.idFirebase].orZero()
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

                mViewModel.sendReceipt(
                    Email(
                        name = concessionaire.name,
                        market = mSelectedMarket.name,
                        linearMeters = participating.linearMeters.toString(),
                        totalAmount = amount.toString(),
                        collectorName = mCollectorName,
                        emailConcessionaire = concessionaire.email
                    )
                )

                if (concessionaire.isForeigner) {
                    mParticipatingConcessMap.remove(concessionaire.idFirebase)
                }

            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }


    private fun taxCollectionDataUpdated(itWasUpdate: Boolean) {
        if (itWasUpdate.not()) {
            val error = getString(R.string.tax_collection_cant_save_info)
            showError(error)
        }
    }

    private fun eventWasReverted(itWas: Boolean) {
        if (itWas.not()) {
            toast(getString(R.string.tax_collection_cant_revert))
        }
    }

    private fun revertCollection(event: EventRE) {
        val concessionaire = mConcessionairesMap[event.idConcessionaire]

        if (concessionaire != null) {

            mTotalAmount -= event.amount

            updateProgress()

            mOpenedTaxCollection.totalAmount = mTotalAmount
            mViewModel.updateTaxCollection(mOpenedTaxCollection)

            mConcessionairesMap[event.idConcessionaire]?.wasPaid = false
        }
    }

    private fun sendReceipt(email: Email) {
        val body = getString(
            R.string.tax_collection_receipt,
            email.name,
            email.market,
            email.linearMeters,
            email.totalAmount,
            mCollectorName
        )

        GlobalScope.launch {
            EmailSender.send(
                mutableListOf(
                    EmailObject(
                        subject = getString(
                            R.string.tax_collection_receipt_subject,
                            CalendarUtils.getCurrentTimeStamp(FORMAT_DATE)
                        ),
                        body = body,
                        recipient = email.emailConcessionaire
                    )
                )
            ) {
                if (it.first.not()) {
                    showError(it.second)
                }
            }
        }
    }

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
                toast(getString(R.string.tax_collection_unknow_event))
            }
        }

        setLabelTexts(
            name = "",
            linearMeters = "",
            lineBusiness = "",
            amount = "",
            status = "",
            absences = 0
        )
    }

    private fun isAddedMarket(isAdded: Boolean) {
        if (isAdded) {
            if (mIsOnLineMode)
                mViewModel.getConcessionairesFEList()
            else
                populateMaps()

        } else {
            showError(getString(R.string.tax_collection_error_saving_info_market))
        }
    }

    private fun showAlertFinalize() {

        if (mViewModel.hasEvents(mOpenedTaxCollection.idFirebase)) {
            launchFinalizeFragment()
            return
        }

        val dialog = BaseDialog(
            imageResource = R.drawable.ic_ops_delete,
            context = this@TaxCollectionActivity,
            mTitle = getString(R.string.common_atention),
            mDescription = getString(R.string.tax_collection_delete_collection),
            buttonYesText = getString(R.string.common_accept),
            buttonNoText = getString(R.string.common_cancel),
            yesAction = { mViewModel.deleteTaxCollection(mOpenedTaxCollection) },
        )

        dialog.show()
    }

    private fun resetActivity() {
        mConcessionairesFEList.clear()
        mConcessionairesMap.clear()
        mParticipatingConcessMap.clear()

        toast(getString(R.string.tax_collection_collection_deleted))

        bsdPickMarket()
    }

    private fun launchFinalizeFragment() {
        val finalizeCollection = FinalizeTaxCollectionFragment.FinalizeCollection(
            type = "create",
            idMarket = mSelectedMarket.idFirebase,
            marketName = mSelectedMarket.name,
            collector = mCollectorName,
            totalAmount = mTotalAmount,
            taxCollection = mOpenedTaxCollection,
            absences = mConcessionairesMap.filterValues {
                it.wasPaid.not() && mParticipatingConcessMap.containsKey(it.idFirebase)
            }
                .map { it.value } as MutableList<ConcessionaireSE>
        )


        launchFragment(
            fragment = FinalizeTaxCollectionFragment(),
            fragmentManager = supportFragmentManager,
            containerId = R.id.containerTaxCollection,
            bundle = Bundle().apply { putParcelable("finalizeCollection", finalizeCollection) }
        )
    }

    data class Email(
        val name: String,
        val market: String,
        val linearMeters: String,
        val totalAmount: String,
        val collectorName: String,
        val emailConcessionaire: String
    )

}