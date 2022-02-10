package com.example.biz_home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.biz_home.api.HomeApi
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.restful.annotation.CacheStrategy.Companion.CACHE_FIRST
import com.example.library.restful.annotation.CacheStrategy.Companion.NET_ONLY
import com.example.common.http.ApiFactory
 import com.example.biz_home.model.HomeModel
import com.example.biz_home.model.TabCategory

class HomeViewModel(private val savedState: SavedStateHandle) : ViewModel() {
    companion object {
        const val SAVE_CATEGORY_TABS_KEY = "save_category_tabs_key"
        const val SAVE_CATEGORY_LIST_KEY = "save_category_list_key"
    }

    fun queryCategoryTabs(): LiveData<List<TabCategory>?> {
        val homeLiveData = MutableLiveData<List<TabCategory>?>()
        val memoryCache = savedState.get<List<TabCategory>?>(SAVE_CATEGORY_TABS_KEY)
        if (memoryCache != null) {
            homeLiveData.postValue(memoryCache)
            return homeLiveData
        }
        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : HiCallBack<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        homeLiveData.postValue(data)
                        savedState.set(SAVE_CATEGORY_TABS_KEY, data)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    homeLiveData.postValue(null)
                }
            })
        return homeLiveData
    }


    fun queryTabCategoryList(
        categoryId: String?,
        pageIndex: Int,
        cacheStrategy: Int
    ): LiveData<HomeModel?> {
        val liveData = MutableLiveData<HomeModel?>()
        val memoryCache = savedState.get<HomeModel>(SAVE_CATEGORY_LIST_KEY)
        if (memoryCache != null && pageIndex == 1 && cacheStrategy == CACHE_FIRST) {
            liveData.postValue(memoryCache)
            return liveData
        }
        ApiFactory.create(HomeApi::class.java)
            .queryTabCategoryList(cacheStrategy, categoryId!!, pageIndex, 10)
            .enqueue(object : HiCallBack<HomeModel> {
                override fun onSuccess(response: HiResponse<HomeModel>) {
                    val data = response.data
                    if (response.successful() && response.data != null) {
                        //一次缓存数据，一次接口数据
                        liveData.postValue(data)
                        //只有在刷新的时候，且不是本地缓存的数据 才存储到内容中
                        if (cacheStrategy != NET_ONLY && response.code == HiResponse.SUCCESS) {
                            savedState.set("categoryList", data)
                        }

                    } else {
                        liveData.postValue(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }
}