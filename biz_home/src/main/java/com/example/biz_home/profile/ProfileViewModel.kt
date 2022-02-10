package com.example.biz_home.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.biz_home.api.AccountApi
import com.example.biz_home.model.CourseNotice
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.common.http.ApiFactory

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