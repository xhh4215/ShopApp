package com.example.common.city

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.GET

internal interface CityApi {
    @GET("cities")
    fun listCities(): HiCall<CityModel>
}
