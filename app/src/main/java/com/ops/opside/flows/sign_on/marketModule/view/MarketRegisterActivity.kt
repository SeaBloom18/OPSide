package com.ops.opside.flows.sign_on.marketModule.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.Constants
import com.ops.opside.common.utils.Formaters.orZero
import com.ops.opside.databinding.ActivityMarketRegisterBinding
import com.ops.opside.flows.sign_on.marketModule.viewModel.MarketRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MarketRegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMarketRegisterBinding
    private val concessionaires = arrayOf("David", "Mario", "Juan", "Luis")

    private val mViewModel: MarketRegisterViewModel by viewModels()
    private var mMarketFE: MarketFE = MarketFE()
    private var mMarketSE: MarketSE? = null
    private var latitudeMaps = 0.0
    private var longitudeMaps = 0.0

    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts
        .RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            finish()
        }
        }
    }

    private val mapResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.apply {
                latitudeMaps = getStringExtra("latitude")?.toDouble().orZero()
                longitudeMaps = getStringExtra("longitude")?.toDouble().orZero()
                Toast.makeText(this@MarketRegisterActivity, "Direccion selecciona correctamente!", Toast.LENGTH_SHORT).show()
                mBinding.btnSelectLocation.text = "Edit Location"
                val addresses: List<Address>
                val geocoder = Geocoder(this@MarketRegisterActivity, Locale.getDefault())

                addresses = geocoder.getFromLocation(latitudeMaps, longitudeMaps, 1)

                val address: String = addresses[0].getAddressLine(0)

                val city: String = addresses[0].getLocality()
                val state: String = addresses[0].getAdminArea()
                val country: String = addresses[0].getCountryName()
                val postalCode: String = addresses[0].getPostalCode()
                val knownName: String = addresses[0].getFeatureName()
                mBinding.tvAddressSelection.text = address
            }
        } else {
            Toast.makeText(this, "Direccion no seleccionada!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMarketRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnViewConce.setOnClickListener { viewConcessionaire() }
            btnSelectLocation.setOnClickListener {
                val intent = Intent(this@MarketRegisterActivity, MarketLocationActivity::class.java)
                mapResult.launch(intent)
            }
            btnSaveMarket.setOnClickListener {
                saveMarket()
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))


        bindViewModel()
        setToolbar()
        editModeMarketValidation()
    }


    /** ViewModel SetUp **/
    private fun bindViewModel(){

    }

    /** Toolbar Menu and backPressed **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_global_common, null)

        val btnFinish = view.findViewById<MaterialButton>(R.id.btnClose)
        btnFinish.setText(Constants.BOTTOM_SHEET_BTN_CLOSE_APP)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.setText(Constants.BOTTOM_SHEET_TV_CLOSE_APP)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setToolbar(){
        with(mBinding.toolbar.commonToolbar) {
            this.title = getString(R.string.registration_market_create_toolbar_title)
            setSupportActionBar(this)
            (context as MarketRegisterActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    /** Other Methods **/
    private fun saveMarket() {
        if (mBinding.teMarketName.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Debes de escribir un nombre para el tianguis",
                Toast.LENGTH_SHORT).show()
        } else {
            with(mMarketFE){
                name = mBinding.teMarketName.text.toString().trim()
                address = "Direccion generica"
                latitude = latitudeMaps
                longitude = longitudeMaps
                concessionaires = mutableListOf()
            }
            if (mMarketSE != null){
                mViewModel.updateMarket(mMarketSE!!.idFirebase, mMarketFE.name, mMarketFE.address)
                finish()
            }
            else{
                mViewModel.insertMarket(mMarketFE)
                finish()
            }
            Log.d("insertMarketSuccess", mMarketFE.toString())
        }
    }

    private fun setFieldsIsEditMode(marketSE: MarketSE){
        Log.d("marketSerializable", marketSE.toString())
        mBinding.teMarketName.setText(marketSE.name)
        mBinding.tvAddressSelection.text = marketSE.address
        mBinding.tvConcessionaireNumber.text = marketSE.numberConcessionaires.toString()
    }

    private fun viewConcessionaire() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_concess, null)

        val autoCompUserName = view.findViewById<AutoCompleteTextView>(R.id.acUserName)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, concessionaires)
        autoCompUserName.setAdapter(adapter)

        val group = view.findViewById<Group>(R.id.groupConce)

        val textViewList = view.findViewById<TextView>(R.id.tvViewAllConcess)
        textViewList.setOnClickListener {
            if (group.visibility == View.VISIBLE){ //Si la vista esta oculta
                //TransitionManager.beginDelayedTransition(holder.binding.marketCardView)
                group.visibility = View.GONE
                //holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_up)
            } else{ //Si la vista esta expuesta
                //TransitionManager.endTransitions(holder.binding.marketCardView)
                group.visibility = View.VISIBLE
                //holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_down)
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun editModeMarketValidation() {
        mMarketSE = intent.getSerializableExtra("market") as? MarketSE
        if (mMarketSE != null) {
            mBinding.toolbar.commonToolbar.title = getString(R.string.registration_market_edit_toolbar_title)
            setFieldsIsEditMode(mMarketSE!!)
        }
    }
}