package com.example.common.http

import com.example.library.restful.HiConvert
import com.example.library.restful.HiResponse
import com.google.gson.Gson
  import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.lang.reflect.Type

/***
 * @author 栾桂明
 * @desc 使用Gson对返回的数据进行统一的转换
 * @date 2021年1月3日
 */
class GsonConvert : HiConvert {
    private val gson: Gson = Gson()

    override fun <T> convert(rawData: String, dataType: Type): HiResponse<T> {
        var response: HiResponse<T> = HiResponse<T>()
        try {
            val jsonObject = JSONObject(rawData)
            response.code = jsonObject.optInt("code")
            response.msg = jsonObject.optString("msg")
            val data = jsonObject.opt("data")
            if ((data is JSONObject) or (data is JSONArray)) {
                if (response.code == HiResponse.SUCCESS) {
                    response.data = gson.fromJson(data.toString(), dataType)
                } else {
                    response.errorData = gson.fromJson<MutableMap<String, String>>(
                        data.toString(),
                        object : TypeToken<MutableMap<String, String>>() {}.type
                    )
                }
            } else {
                response.data = data as T?
            }
        } catch (e: Exception) {
            e.printStackTrace()
            response.code = -1
            response.msg = e.message
        }
        response.rawData = rawData
        return response
    }
}