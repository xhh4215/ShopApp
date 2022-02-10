package com.example.biz_home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.biz_home.api.CategoryApi
import com.example.biz_home.model.Subcategory
import com.example.biz_home.model.TabCategory
import com.example.common.http.ApiFactory
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse

class CategoryViewModel : ViewModel() {
    fun queryCategoryList(): LiveData<List<TabCategory>?> {
        val tabCategoryData = MutableLiveData<List<TabCategory>?>()

        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(simpleCallback(tabCategoryData))
        return tabCategoryData
    }
    fun querySubcategoryList(categoryId: String): LiveData<List<Subcategory>?> {
        val subcategoryListData = MutableLiveData<List<Subcategory>?>()
        ApiFactory.create(CategoryApi::class.java).querySubcategoryList(categoryId)
            .enqueue(simpleCallback(subcategoryListData))

        return subcategoryListData
    }

    private fun <T> simpleCallback(liveData: MutableLiveData<T?>): HiCallBack<T> {
        return object : HiCallBack<T> {
            override fun onSuccess(response: HiResponse<T>) {
                if (response.successful() && response.data != null) {
                    liveData.postValue(response.data)
                } else {
                    liveData.postValue(null)
                }
            }

            override fun onFailed(throwable: Throwable) {
                liveData.postValue(null)
            }
        }
    }

}
