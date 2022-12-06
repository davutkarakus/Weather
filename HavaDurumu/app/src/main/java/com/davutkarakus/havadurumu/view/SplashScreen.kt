package com.davutkarakus.havadurumu.view

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.davutkarakus.havadurumu.R
import com.davutkarakus.havadurumu.viewmodel.havaDurumViewModel
import kotlinx.android.synthetic.main.activity_splash_screen2.*
import java.time.Duration
import java.util.*

class SplashScreen : AppCompatActivity() {
    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager
    private lateinit var viewModel: havaDurumViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)
        viewModel = ViewModelProvider(this).get(havaDurumViewModel::class.java)
        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var AppId = "47b3cb4a3b806f58ef306b42149dfb57"
        locationListener=object : LocationListener {

            override fun onLocationChanged(location: Location) {
                val geocoder = Geocoder(this@SplashScreen, Locale.getDefault())
                val adresListesi = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                var cityname = adresListesi.get(0).adminArea
                viewModel.refreshData(cityname,AppId)
                if(cityname!=null){
                    val i =Intent(this@SplashScreen,MainActivity::class.java)
                    i.putExtra("cityname",cityname)
                    startActivity(i)
                }else{
                    progressBar.visibility=View.INVISIBLE
                    SplashHataText.visibility=View.VISIBLE
                    tryAgainText.visibility=View.VISIBLE
                }
            }
        }
      if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

        }else{
            //izin zaten verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,50f,locationListener)
          progressBar.max=10
          val currentProgress=10
          ObjectAnimator.ofInt(progressBar,"progress",currentProgress).setDuration(4000).start()

        }


    }



   override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size >0){
                if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    //izin verildi
                    progressBar.visibility=View.VISIBLE
                    SplashHataText.visibility=View.INVISIBLE
                    tryAgainText.visibility=View.INVISIBLE
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
                    progressBar.max=10
                    val currentProgress=10
                        ObjectAnimator.ofInt(progressBar,"progress",currentProgress).setDuration(4000).start()

                }
                if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    progressBar.max=10
                    var currentProgress=10
                    ObjectAnimator.ofInt(progressBar,"progress",currentProgress).setDuration(4000).start()
                    Handler().postDelayed({
                        progressBar.visibility=View.INVISIBLE
                        SplashHataText.visibility=View.VISIBLE
                        tryAgainText.visibility=View.VISIBLE
                        ObjectAnimator.ofInt(progressBar,"progress",1).start()
                    },4500)
                    tryAgainText.setOnClickListener {


                            //izin verilmemiş
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)}

                }

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }





}