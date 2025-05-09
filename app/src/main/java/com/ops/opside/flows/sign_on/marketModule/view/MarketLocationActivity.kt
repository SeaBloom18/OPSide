package com.ops.opside.flows.sign_on.marketModule.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.PUT_EXTRA_LATITUDE
import com.ops.opside.common.entities.PUT_EXTRA_LONGITUDE
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityMarketLocationBinding

class MarketLocationActivity : BaseActivity(), OnMapReadyCallback{

    private lateinit var mBinding: ActivityMarketLocationBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var mGlobalMarker: MarkerOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMarketLocationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        dialogMapsAlertLocation()

        with(mBinding){
            btnSaveLocation.setOnClickListener { dialogConfirmLocation() }
            ibLocationClose.setOnClickListener { finish() }
        }

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    /** Other Methods **/
    private fun dialogConfirmLocation() {
        val dialog = BaseDialog(
            this@MarketLocationActivity,
            R.drawable.ic_ops_btn_confirm,
            getString(R.string.dialog_title_confirm_location),
            getString(R.string.dialog_message_confirm_location),
            getString(R.string.common_accept),
            "",
            {
                val intent = Intent()
                if (latitude > 0.0) {
                    intent.putExtra(PUT_EXTRA_LATITUDE, latitude.toString())
                    intent.putExtra(PUT_EXTRA_LONGITUDE, longitude.toString())
                    intent.data = Uri.parse(intent.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    intent.putExtra(PUT_EXTRA_LATITUDE, currentLocation.latitude.toString())
                    intent.putExtra(PUT_EXTRA_LONGITUDE, currentLocation.longitude.toString())
                    intent.data = Uri.parse(intent.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            },)
        dialog.show()
    }

    private fun dialogMapsAlertLocation(){
        val dialog = BaseDialog(
            this@MarketLocationActivity,
            R.drawable.ic_item_gps,
            getString(R.string.dialog_title_before_select_location),
            getString(R.string.dialog_message_before_select_location),
            getString(R.string.common_accept)
        )
        dialog.show()
    }

    /** GoogleMaps SetUp **/
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
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        mGlobalMarker = MarkerOptions().position(latLng).title(getString(R.string.google_maps_market_title)).draggable(true)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f))

        googleMap.addMarker(mGlobalMarker)

        googleMap.setOnCameraMoveListener {
            googleMap.clear()
            mGlobalMarker = MarkerOptions().position(googleMap.cameraPosition.target).title(getString(R.string.google_maps_market_title))
            googleMap.addMarker(mGlobalMarker)

            latitude = mGlobalMarker.position.latitude
            longitude = mGlobalMarker.position.longitude
        }
    }

    /** Permissions **/
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