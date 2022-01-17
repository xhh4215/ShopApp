package com.example.shopapp.http

import com.example.library.restful.HiRestful
import com.example.shopapp.http.api.HttpStatusInterceptor

object ApiFactory {
    private const val baseUrl = "https://api.devio.org/as/"
    private val hiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))
    init {
        hiRestful.addInterceptor(BizInterceptor())
        hiRestful.addInterceptor(HttpStatusInterceptor())
    }

    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}