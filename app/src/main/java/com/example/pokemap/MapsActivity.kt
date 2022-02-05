package com.example.pokemap

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    val Fine_Access:Int = 1
    var location:Location? = null

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Toast.makeText(this, "create", Toast.LENGTH_LONG).show()
        checkPermission()
    }

    fun checkPermission(){
        Toast.makeText(this, "check", Toast.LENGTH_LONG).show()
        if(Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),Fine_Access)
                return
            }
        }
        getUserLocation()
    }

    fun getUserLocation(){
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show()
        //TODO: implement later
        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var markerUpdate = myThread()
        markerUpdate.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            Fine_Access->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }else{
                    Toast.makeText(this, "cant access location", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }

    inner class MyLocationListener:LocationListener{

        constructor(){
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location = p0
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //TODO("Not yet implemented")
        }

        override fun onProviderEnabled(provider: String?) {
            //TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
            //TODO("Not yet implemented")
        }

    }

    inner class myThread:Thread{
        constructor():super(){

        }

        override fun run() {
            while (true){
                try {
                    runOnUiThread {
                        val userLocation = LatLng(location!!.latitude, location!!.longitude)
                        mMap.clear()
                        mMap.addMarker(
                            MarkerOptions()
                                .position(userLocation)
                                .title("Me")
                                .snippet(" current location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.megaman))
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10f))
                        Thread.sleep(10000)
                    }
                }catch (e:Exception){

                }

            }

        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

}
