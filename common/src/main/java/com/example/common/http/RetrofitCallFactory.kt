package com.example.common.http

import com.example.library.restful.*
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.lang.IllegalStateException

class RetrofitCallFactory(baseUrl: String) : HiCall.Factory {
    private val apiService: ApiService
    private val hiConvert: HiConvert

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        hiConvert = GsonConvert()
    }

    override fun newCall(request: HiRequest): HiCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            val realCall = createRealCall(request)
            val response = realCall.execute()
            return parseResponse(response)
        }

        override fun enqueue(callBack: HiCallBack<T>) {
            val realCall = createRealCall(request)
            realCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val response = parseResponse(response = response)
                    callBack.onSuccess(response)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callBack.onFailed(t)
                }

            })
            return
        }

        private fun parseResponse(response: Response<ResponseBody>): HiResponse<T> {
            var rawData: String? = null
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    rawData = body.string()
                }
            } else {
                val body = response.errorBody()
                if (body != null) {
                    rawData = body.string()
                }
            }

            return hiConvert.convert(rawData!!, request.returnType!!)
        }

        private fun createRealCall(request: HiRequest): Call<ResponseBody> {
            when (request.httpMethod) {
                HiRequest.METHOD.GET -> {
                    return apiService.get(
                        request.headers,
                        request.endPointUrl(),
                        request.parameters
                    )
                }
                HiRequest.METHOD.POST -> {
                    val requestBody: RequestBody = buildRequestBody(request)
                    return apiService.post(request.headers, request.endPointUrl(), requestBody)
                }
                HiRequest.METHOD.PUT->{
                    val requestBody: RequestBody = buildRequestBody(request)
                    return apiService.put(request.headers, request.endPointUrl(), requestBody)
                }
                HiRequest.METHOD.DELETE->{
                    return apiService.delete(request.headers,request.endPointUrl())
                }
                else -> {
                    throw IllegalStateException("hirestful only support get and post ")
                }
            }
        }


        private fun buildRequestBody(request: HiRequest): RequestBody {
            val parameters: MutableMap<String, String>? = request.parameters
            val builder = FormBody.Builder()
            val requestBody: RequestBody
            val jsonObject = JSONObject()
            if (parameters != null) {
                for ((key, value) in parameters) {
                    if (request.formPost) {
                        builder.add(key, value)
                    } else {
                        jsonObject.put(key, value)
                    }
                }
            }
            if (request.formPost) {
                requestBody = builder.build()
            } else {
                //bugfix: reuqest-header   content-type="application/json; charset=utf-8"
                requestBody = RequestBody.create(
                    MediaType.parse("application/json;charset=utf-8"),
                    jsonObject.toString()
                )
            }
            return requestBody
        }
    }


    interface ApiService {
        @GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>?
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @Body body: RequestBody
        ): Call<ResponseBody>

        @PUT
        fun put(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>

        @DELETE//???????????????requestbody
        fun delete(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String
        ): Call<ResponseBody>
    }
}