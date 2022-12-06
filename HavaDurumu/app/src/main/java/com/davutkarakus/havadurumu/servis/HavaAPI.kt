package com.davutkarakus.havadurumu.servis


import com.davutkarakus.havadurumu.model1.HavaBilgi
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HavaAPI {
    //https://api.openweathermap.org/data/2.5/weather?lat=37.0407&lon=35.2622&appid=47b3cb4a3b806f58ef306b42149dfb57
    //BASE_URL https://api.openweathermap.org/
    //data/2.5/weather?lat=37.0407&lon=35.2622&appid=47b3cb4a3b806f58ef306b42149dfb57


    @GET("data/2.5/weather?")
    fun getHavaBilgi(@Query("q") cityname:String, @Query("APPID") app_id:String): Single<HavaBilgi>

}