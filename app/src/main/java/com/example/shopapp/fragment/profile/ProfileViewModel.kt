package com.example.shopapp.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.AccountApi
import com.example.shopapp.model.CourseNotice

class ProfileViewModel : ViewModel() {
    fun queryCourseNotice(): LiveData<CourseNotice?> {
        val noticeData = MutableLiveData<CourseNotice?>()
        ApiFactory.create(AccountApi::class.java).notice().enqueue(object :
            HiCallBack<CourseNotice> {
            override fun onSuccess(response: HiResponse<CourseNotice>) {
                if (response.successful() && response.data != null) {
                    noticeData.postValue(response.data)
                } else {
                    noticeData.postValue(null)
                }

            }
            override fun onFailed(throwable: Throwable) {
                noticeData.postValue(null)
            }
        })
        return noticeData
    }
}