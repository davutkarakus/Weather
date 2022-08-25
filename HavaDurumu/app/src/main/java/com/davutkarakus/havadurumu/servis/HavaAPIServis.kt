package com.davutkarakus.havadurumu.servis

import com.davutkarakus.havadurumu.model1.Coord
import com.davutkarakus.havadurumu.model1.HavaBilgi
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HavaAPIServis {

    //https://api.openweathermap.org/data/2.5/weather?lat=37.0407&lon=35.2622&appid=47b3cb4a3b806f58ef306b42149dfb57
    //BASE_URL https://api.openweathermap.org/
    //data/2.5/weather?lat=37.0407&lon=35.2622&appid=47b3cb4a3b806f58ef306b42149dfb57
     val BASE_URL="https://api.openweathermap.org/"
     val api=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(HavaAPI::class.java)

    fun getData(lat:String,lon:String,app_id:String): Single<HavaBilgi> {
        return api.getHavaBilgi(lat,lon,app_id)

    }


}