package com.example.shopapp.http

import com.example.common.utils.SPUtil
import com.example.library.restful.HiRestful
import com.example.shopapp.http.api.HttpStatusInterceptor

object ApiFactory {
    private const val KEY_DEGRADE_HTTP = "degrade_http"
    private const val HTTPS_BASE_URL = "https://api.devio.org/as/"
    private const val HTTP_BASE_URL = "http://api.devio.org/as/"
    private val degrade2Http = SPUtil.getBoolean(KEY_DEGRADE_HTTP)
    private val baseUrl = if (degrade2Http) HTTP_BASE_URL else HTTPS_BASE_URL
    private val hiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))
    init {
        hiRestful.addInterceptor(BizInterceptor())
        hiRestful.addInterceptor(HttpStatusInterceptor())
        SPUtil.putBoolean(KEY_DEGRADE_HTTP,false)
    }
    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}