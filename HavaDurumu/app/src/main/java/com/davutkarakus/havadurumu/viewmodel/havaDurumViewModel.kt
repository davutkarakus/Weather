package com.davutkarakus.havadurumu.viewmodel

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davutkarakus.havadurumu.model1.HavaBilgi
import com.davutkarakus.havadurumu.servis.HavaAPIServis
import com.davutkarakus.havadurumu.view.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class havaDurumViewModel:ViewModel() {
    val bilgiler=MutableLiveData<HavaBilgi>()
    val hataMesaji=MutableLiveData<Boolean>()
    val yukleniyor=MutableLiveData<Boolean>()
    private val havaApiServis=HavaAPIServis()
    val disposable=CompositeDisposable()


    fun refreshData(lat:String,lon:String,app_id:String){
       verileriInternettenAl(lat,lon, app_id)
    }
    private fun verileriInternettenAl(lat:String,lon:String,app_id:String){
        yukleniyor.value=true
        hataMesaji.value=false
        disposable.add(
            havaApiServis.getData(lat,lon,app_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<HavaBilgi>(){
                    override fun onSuccess(t: HavaBilgi) {

                      bilgiler.value=t
                        hataMesaji.value=false
                        yukleniyor.value=false

                    }

                    override fun onError(e: Throwable) {
                        hataMesaji.value=true
                        yukleniyor.value=false
                    }

                })
        )
    }


}