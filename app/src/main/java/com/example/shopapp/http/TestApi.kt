package com.example.shopapp.http

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.Field
import com.example.library.restful.annotation.GET
import org.json.JSONObject

interface TestApi {
    @GET("cities")
    fun listCities(@Field("name") name: String): HiCall<JSONObject>
}