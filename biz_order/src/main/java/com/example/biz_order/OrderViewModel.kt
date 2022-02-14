package com.example.biz_order

 import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.biz_order.api.AddressApi
 import com.example.biz_order.model.Address
 import com.example.biz_order.model.AddressModel
import com.example.common.http.ApiFactory
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse

class OrderViewModel : ViewModel() {
    fun queryMainAddress(): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java).queryAddress(1, 1)
            .enqueue(object : HiCallBack<AddressModel> {
                override fun onSuccess(response: HiResponse<AddressModel>) {
                    val list = response.data?.list
                    val firstElement = if (list?.isNotEmpty() == true) list[0] else null
                    liveData.postValue(firstElement)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }
}