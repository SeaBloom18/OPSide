package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.utils.launchFragment
import com.ops.opside.common.utils.showError
import com.ops.opside.common.utils.showLoading
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
    private lateinit var mConcessionairesMap: MutableMap<String, ConcessionaireSE>

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
        setUpPieChart()
    }

    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))
        mViewModel.getConcessionairesFEList.observe(this, Observer(this::getConcessionairesFEList))
        mViewModel.persistConcessionairesSEList.observe(this, Observer(this::persistConcessionairesSEList))
        mViewModel.getConcessionairesSEList.observe(this, Observer(this::getConcessionairesSEList))
        mViewModel.persistMarketSE.observe(this, Observer(this::isAddedMarket))
        mViewModel.getConcessionairesSEList.observe(this, Observer(this::getAllConcessionaires))
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

    private fun isAddedMarket(isAdded: Boolean){
        if (isAdded){
            mViewModel.getConcessionairesFEList(mSelectedMarket.idFirebase)
        } else{
            showError("Hubo un error al guardar la información del Tianguis seleccionado")
        }
    }

    private fun setUpPieChart() {
        mBinding.chartTaxMoney.apply {
            progress = 65f
            setProgressWithAnimation(0f, 4000) // =1s

            // Set Progress Max
            progressMax = 100f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = getColor(R.color.secondaryColor)
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = getColor(R.color.primaryLightColor)
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 28f // in DP
            backgroundProgressBarWidth = 30f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }

    private fun getConcessionairesFEList(concessionairesList: MutableList<ConcessionaireFE>) {
        mConcessionairesFEList = concessionairesList
        mViewModel.persistConcessionairesSEList(mSelectedMarket.idFirebase, mConcessionairesFEList)
    }

    private fun getConcessionairesSEList(concessionairesList: MutableList<ConcessionaireSE>) {
        Log.d("Demo", concessionairesList.toString())
    }

    private fun getAllConcessionaires(concessionairesList: MutableList<ConcessionaireSE>){
        mConcessionairesMap = mutableMapOf()
        concessionairesList.map {
            mConcessionairesMap.put(it.idFirebase, it)
        }
    }

    private fun persistConcessionairesSEList(wasAdded: Boolean) {
        if (wasAdded) {
            mViewModel.getAllConcessionaires()
        } else{
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

    private fun setConcessionaireData(id: String){
        val concessionaire = mConcessionairesMap[id]
        concessionaire?.let {
            with(mBinding){
                etDealerName.setText(it.name)
            }
        }
    }

}