package com.ops.opside.flows.sign_on.marketModule.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import com.ops.opside.R
import com.ops.opside.databinding.ActivityMarketLocationBinding
import kotlinx.coroutines.*

class MarketLocationActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mBinding: ActivityMarketLocationBinding

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMarketLocationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        with(mBinding){
            btnSaveLocation.setOnClickListener {
                /*val data = Intent(this@MarketLocationActivity, MarketRegisterActivity::class.java)
                val text = currentLocation
                data.putExtra("latitude", currentLocation.latitude)
                data.putExtra("longitude", currentLocation.longitude)
                data.putExtra("currentLocation", currentLocation)
                Toast.makeText(this@MarketLocationActivity, "${currentLocation.latitude} ${currentLocation.longitude}", Toast.LENGTH_SHORT).show()
                data.data = Uri.parse(text.toString())
                setResult(RESULT_OK, data)*/

                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        val data = Intent()
                        val text = currentLocation
                        data.putExtra("latitude", currentLocation.latitude)
                        data.putExtra("longitude", currentLocation.longitude)
                        data.putExtra("currentLocation", currentLocation)
                        Toast.makeText(this@MarketLocationActivity, "${currentLocation.latitude} ${currentLocation.longitude}", Toast.LENGTH_SHORT).show()
                        data.data = Uri.parse(text.toString())
                        setResult(RESULT_OK, data)
                    }
                    delay(200L)
                    finish()
                }
            }
            ibLocationClose.setOnClickListener { finish() }
        }

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
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
                Toast.makeText(applicationContext, currentLocation.latitude.toString() + " " +
                        currentLocation.longitude, Toast.LENGTH_SHORT).show()
                //Toast.makeText(applicationContext, "${currentLocation}", Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }
}