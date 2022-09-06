package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.zxing.integration.android.IntentIntegrator
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.utils.Formaters.orZero
import com.ops.opside.databinding.ActivityTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.interfaces.TaxCollectionAux
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.TaxCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaxCollectionActivity : AppCompatActivity(), TaxCollectionAux {

    private val mBinding: ActivityTaxCollectionBinding by lazy {
        ActivityTaxCollectionBinding.inflate(layoutInflater)
    }

    private val mViewModel: TaxCollectionViewModel by viewModels()

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
        }

        bindViewModel()
        bsdPickMarket()
        getPriceLinearMeter()
        setUpPieChart()
    }

    private fun getPriceLinearMeter() {
        mPriceLinearMeter = mViewModel.getPriceLinearMeter()
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

        mViewModel.persistMarketSE.observe(this, Observer(this::isAddedMarket))
    }

    private fun populateMaps() {
        mViewModel.getAllConcessionaires()
    }

    private fun showAlertFinalize() {
        val dialog = BaseDialog(
            this@TaxCollectionActivity,
            getString(R.string.common_atention),
            getString(R.string.tax_collection_finalize_collection),
            getString(R.string.common_accept),
            "",
            { launchFinalizeFragment() }
        )
        dialog.show()
    }

    private fun launchFinalizeFragment() {
        val bundle = Bundle()
        bundle.putString("type", "create")

        launchFragment(
            FinalizeTaxCollectionFragment(),
            supportFragmentManager,
            R.id.containerTaxCollection,
            bundle
        )
    }

    private fun bsdPickMarket() {
        val dialog = BottomSheetPickMarket {
            mSelectedMarket = it
            mBinding.tvMarket.text = mSelectedMarket.name

            mViewModel.addMarket(mSelectedMarket)
        }

        dialog.isCancelable = false
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun isAddedMarket(isAdded: Boolean) {
        if (isAdded) {
            if (mIsOnLineMode)
                mViewModel.getConcessionairesFEList(mSelectedMarket.idFirebase)
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

    private fun setProgress(amount: Double) {
        mBinding.chartTaxMoney.setProgressWithAnimation((amount).toFloat(), 4000)

        mTotalAmount += amount
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
        val dialog = BottomSheetRecordTaxCollection()
        dialog.show(supportFragmentManager, dialog.tag)
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
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                setConcessionaireData(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setConcessionaireData(id: String) {
        if (mConcessionairesMap.containsKey(id)) {
            if (mParticipatingConcessMap.containsKey(id)) {
                val concessionaire = mConcessionairesMap[id]
                val participating = mParticipatingConcessMap[id]

                if (concessionaire != null && participating != null) {
                    with(mBinding) {
                        tryOrPrintException {
                            etDealerName.setText(concessionaire.name)
                            etLinearMeters.setText(participating.linearMeters.toString())
                            etLineOfBusiness.setText(participating.lineBusiness)
                            etStatus.setText("Completado")
                            etStatus.setTextColor(getColor(R.color.secondaryColor))

                            val amount = (participating.linearMeters * mPriceLinearMeter).orZero()
                            etTotalAmount.setText(amount.toString())

                            setProgress(amount)


                            persistCapturedData(concessionaire, amount)
                        }
                    }
                }

            } else {
                val alert = BaseDialog(
                    this,
                    "Alerta",
                    "El usuario no está dado de alta en este tianguis.\n¿Deseas agregarlo?",
                    getString(R.string.common_accept),
                    getString(R.string.common_cancel),
                    {
                        /*val participatingConcess =
                            ParticipatingConcessRE(
                                mSelectedMarket.idFirebase,
                                id,
                                "",

                            )
                        addConcessionaireToMarket*/

                        val dialog = BottomSheetRelateConcessMarket(
                            mConcessionairesMap[id]!!, mSelectedMarket.parseToSE()
                        )

                        dialog.show(supportFragmentManager, dialog.tag)
                    }
                )

                alert.show()
            }

        } else {
            mBinding.etStatus.setText("Usuario no registrado")
            mBinding.etStatus.setTextColor(getColor(R.color.warning))
        }
    }

    private fun addConcessionaireToMarket(participatingConcess: ParticipatingConcessRE) {
        mViewModel.addConcessionaireToMarket(participatingConcess)
    }

    // TODO: Guardar en tabla el usuario recien escaneado
    private fun persistCapturedData(concessionaire: ConcessionaireSE, amount: Double) {

    }

}