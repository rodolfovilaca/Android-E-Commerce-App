package service

import modules.Flower
import retrofit2.Call
import retrofit2.http.GET
import rx.Observable

interface Service {
    @GET("/feeds/flowers.json")
    fun getAllFlowers(): Observable<List<Flower>>
}