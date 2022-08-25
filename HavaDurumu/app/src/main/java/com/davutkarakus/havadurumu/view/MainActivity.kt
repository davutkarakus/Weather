package com.davutkarakus.havadurumu.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.davutkarakus.havadurumu.R
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.davutkarakus.havadurumu.servis.HavaAPIServis
import com.davutkarakus.havadurumu.viewmodel.havaDurumViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:havaDurumViewModel
    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager
    private val havaApiServis=HavaAPIServis()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener=object : LocationListener{
            override fun onLocationChanged(location: Location) {
                progressBar.visibility=View.GONE
                var lat=location.latitude.toString()
                var lon=location.longitude.toString()
                var AppId="47b3cb4a3b806f58ef306b42149dfb57"
                viewModel=ViewModelProvider(this@MainActivity).get(havaDurumViewModel::class.java)
                viewModel.refreshData(lat,lon,AppId)
                swipeRefreshLayout.setOnRefreshListener {
                    progressBar.visibility=View.VISIBLE
                    hataMesajiText.visibility=View.GONE
                    viewModel=ViewModelProvider(this@MainActivity).get(havaDurumViewModel::class.java)
                    viewModel.refreshData(lat,lon,AppId)
                    swipeRefreshLayout.isRefreshing=false
                    Toast.makeText(this@MainActivity,"İnternetten Aldık",Toast.LENGTH_LONG).show()
                    observeLiveData()





                }
                observeLiveData()

            }

        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{
            //izin zaten verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,50f,locationListener)
        }

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.hide()

    }

        fun observeLiveData(){
            viewModel.bilgiler.observe(this, Observer {
                it?.let {


                    AnasicaklikText.text="${(it.main.temp-273).toInt()}°C "
                    sehirText.text=it.name
                    minSicaklikText.text="Min.Temp=${(it.main.tempMin-273).toInt()}°C"
                    maxSicaklikText.text="Max.Temp=${(it.main.tempMax-273).toInt()}°C"
                    sunriseText.text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(it.sys.sunrise*1000).toString()
                    sunsetText.text=SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(it.sys.sunset*1000).toString()
                    windText.text="${it.wind.speed} km/sa"
                    humidityText.text="%${it.main.humidity}"
                    pressureText.text="${it.main.pressure} mb"
                    bulutDurumText.text=it.weather.get(0).description
                    updatedText.text="Updated at : ${SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(it.dt*1000)}"

                }
            })
            viewModel.hataMesaji.observe(this, Observer {
                it?.let {
                    if(it){
                        hataMesajiText.visibility=View.VISIBLE
                        AnasicaklikText.visibility=View.GONE
                        sehirText.visibility=View.GONE
                        minSicaklikText.visibility=View.GONE
                        maxSicaklikText.visibility=View.GONE
                        bulutDurumText.visibility=View.GONE
                        sunriseText.visibility=View.GONE
                        sunsetText.visibility=View.GONE
                        windText.visibility=View.GONE
                        humidityText.visibility=View.GONE
                        pressureText.visibility=View.GONE
                    }else{
                        hataMesajiText.visibility=View.GONE
                        AnasicaklikText.visibility=View.VISIBLE
                        sehirText.visibility=View.VISIBLE
                        minSicaklikText.visibility=View.VISIBLE
                        maxSicaklikText.visibility=View.VISIBLE
                        bulutDurumText.visibility=View.VISIBLE
                        sunriseText.visibility=View.VISIBLE
                        sunsetText.visibility=View.VISIBLE
                        windText.visibility=View.VISIBLE
                        humidityText.visibility=View.VISIBLE
                        pressureText.visibility=View.VISIBLE
                    }
                }
            })
            viewModel.yukleniyor.observe(this, Observer {
                it?.let {
                    if(it){

                        hataMesajiText.visibility=View.GONE
                        progressBar.visibility=View.VISIBLE

                    }else{
                        progressBar.visibility=View.GONE
                    }
                }
            })
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size >0){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    //izin verildi
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}