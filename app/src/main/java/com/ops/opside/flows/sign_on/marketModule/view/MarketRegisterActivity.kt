package com.ops.opside.flows.sign_on.marketModule.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.ops.opside.R
import androidx.lifecycle.Observer
import com.ops.opside.common.entities.PUT_EXTRA_LATITUDE
import com.ops.opside.common.entities.PUT_EXTRA_LONGITUDE
import com.ops.opside.common.entities.PUT_EXTRA_MARKET
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.Formaters.orZero
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityMarketRegisterBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.marketModule.viewModel.ConcessionaireListViewModel
import com.ops.opside.flows.sign_on.marketModule.viewModel.MarketRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MarketRegisterActivity : BaseActivity(), OnMapReadyCallback {

    private val mBinding: ActivityMarketRegisterBinding by lazy {
        ActivityMarketRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var latLng: LatLng
    private var mGoogleMap: GoogleMap? = null
    private var concessionaires = listOf("David", "Alejandro")

    private val mMarketRegViewModel: MarketRegisterViewModel by viewModels()
    private val mConcessionaireListViewModel: ConcessionaireListViewModel by viewModels()
    private var mMarketFE: MarketFE = MarketFE()
    private var mMarketSE: MarketSE? = null

    private var latitudeMaps = 0.0
    private var longitudeMaps = 0.0
    private var addressSelected: String = ""
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    /** Permission Request **/
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

    /** ForActivityResult **/
    private val mapResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.apply {
                latitudeMaps = getStringExtra(PUT_EXTRA_LATITUDE)?.toDouble().orZero()
                longitudeMaps = getStringExtra(PUT_EXTRA_LONGITUDE)?.toDouble().orZero()
                mBinding.btnSelectLocation.text = getString(R.string.btn_text_edit)
                
                val addresses: List<Address>
                val geocoder = Geocoder(this@MarketRegisterActivity, Locale.getDefault())

                addresses = geocoder.getFromLocation(latitudeMaps, longitudeMaps, 1)

                addressSelected = addresses[0].getAddressLine(0)

                mBinding.tvAddressSelection.text = addressSelected
            }
        } else {
            Toast.makeText(this, R.string.toast_location_not_selected, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.apply {
            btnViewConce.setOnClickListener { viewConcessionaire() }
            btnSelectLocation.setOnClickListener {
                val intent = Intent(this@MarketRegisterActivity, MarketLocationActivity::class.java)
                mapResult.launch(intent)
            }
            btnSaveMarket.setOnClickListener {
                if (saveMarket()){
                    if (mMarketSE != null){
                        mMarketRegViewModel.updateMarket(
                            idFirestore = mMarketSE!!.idFirebase,
                            name = mMarketFE.name,
                            address = mMarketFE.address,
                            marketMeters = mMarketFE.marketMeters,
                            latitude = latitudeMaps,
                            longitude = longitudeMaps)
                        finish()
                    } else{
                        mMarketRegViewModel.insertMarket(mMarketFE)
                        finish()
                    }
                }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        bindViewModel()
        setToolbar()
        editModeMarketValidation()
        fetchLocation()
    }

    /** ViewModel SetUp **/
    private fun bindViewModel() {
        mMarketRegViewModel.getShowProgress().observe(this, Observer(this@MarketRegisterActivity::showLoading))
    }

    /** Override Methods **/
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
        btnFinish.setText(R.string.registration_btn_bs_close)
        btnFinish.setOnClickListener { finish() }

        val tvTitle = view.findViewById<TextView>(R.id.tvBSTitle)
        tvTitle.setText(R.string.registration_tv_bs_close)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setToolbar(){
        with(mBinding.toolbarFragMaket.commonToolbar) {
            this.title = getString(R.string.registration_market_create_toolbar_title)
            setSupportActionBar(this)
            (context as MarketRegisterActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    /** Other Methods **/
    private fun saveMarket(): Boolean {
        var isValid = false
        if (mBinding.teMarketName.text.toString().trim().isNotEmpty() &&
            mBinding.teMarketMeters.text.toString().trim().isNotEmpty()){
            if (mBinding.tvAddressSelection.text.isEmpty()){
                Toast.makeText(this, R.string.toast_market_name_validation, Toast.LENGTH_SHORT).show()
            } else {
                with(mMarketFE){
                    name = mBinding.teMarketName.text.toString().trim()
                    address = addressSelected
                    marketMeters = mBinding.teMarketMeters.text.toString().trim().toDouble()
                    latitude = latitudeMaps
                    longitude = longitudeMaps
                    concessionaires = mutableListOf()
                    isValid = true
                    Log.d("insertMarketSuccess", mMarketFE.toString())
                }
            }
        } else Toast.makeText(this, R.string.toast_market_location_validation, Toast.LENGTH_SHORT).show()
        return isValid
    }

    private fun setFieldsIsEditMode(marketSE: MarketSE){
        Log.d("marketSerializable", marketSE.toString())
        mBinding.teMarketName.setText(marketSE.name)
        mBinding.tvAddressSelection.text = marketSE.address
        Log.d("marketMeters", marketSE.marketMeters.toString())
        mBinding.teMarketMeters.setText("${marketSE.marketMeters}")
        mBinding.btnSelectLocation.text = getString(R.string.btn_text_edit)
        latitudeMaps = marketSE.latitude
        longitudeMaps = marketSE.longitude
        addressSelected = marketSE.address
    }

    private fun viewConcessionaire() {
        //mConcessionaireListViewModel.getMarketId(mMarketSE!!.idFirebase)
        /*val dialog = BottomSheetConcessionaireList {
            mMarketSE = it
        }
        dialog.show(supportFragmentManager, dialog.tag)*/
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_show_concess, null)
        val btnDelete = view.findViewById<MaterialButton>(R.id.btnDeleteConcess)
        val btnSeeConce = view.findViewById<MaterialButton>(R.id.btnGoConcess)
        val tvTitle = view.findViewById<MaterialTextView>(R.id.tvBSTitle)

        //Log.d("conce0", mMarketSE!!.numberConcessionaires[0])
        mMarketRegViewModel.getConcessionairesForMarket(mMarketSE!!.idFirebase)
        btnDelete.setOnClickListener {}

        val autoCompUserName = view.findViewById<AutoCompleteTextView>(R.id.acUserName)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, concessionaires)
        autoCompUserName.setAdapter(adapter)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun editModeMarketValidation() {
        mMarketSE = intent.getSerializableExtra(PUT_EXTRA_MARKET) as? MarketSE
        if (mMarketSE != null) {
            mBinding.toolbarFragMaket.commonToolbar.title = getString(R.string.registration_market_edit_toolbar_title)
            setFieldsIsEditMode(mMarketSE!!)
        } else {
            mBinding.btnViewConce.visibility = View.GONE
        }
    }

    /** GoogleMaps SetUp**/
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        if (mMarketSE != null) latLng = LatLng(latitudeMaps, longitudeMaps)
        else latLng = LatLng(20.348917, -103.194615)
        val markerOptions = MarkerOptions().position(latLng).title(getString(R.string.google_maps_market_title))
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.setMinZoomPreference(15.5F)
        googleMap.setMaxZoomPreference(15.5F)
        googleMap.addMarker(markerOptions)
    }

    override fun onResume() {
        super.onResume()
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            latLng = LatLng(latitudeMaps, longitudeMaps)
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            val markerOptions =
                MarkerOptions().position(latLng).title(getString(R.string.google_maps_market_title))
            mGoogleMap?.addMarker(markerOptions)
            Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.mapPreview) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    /** Permissions Result **/
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }
}