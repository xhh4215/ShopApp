package com.example.shopapp.http

import com.example.library.restful.HiRestful

object ApiFactory {
    private const val baseUrl ="https://api.devio.org/as/"
    private val hiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
    }

    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}