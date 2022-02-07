package com.example.shopapp.biz.detail

import android.text.TextUtils
import androidx.lifecycle.*
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.shopapp.BuildConfig
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.DetailApi
import com.example.shopapp.http.api.FavoriteApi
import com.example.shopapp.model.DetailModel
import com.example.shopapp.model.Favorite
import java.lang.Exception

class DetailViewModel(val goodsId: String?) : ViewModel() {
    companion object {
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    val constructor = modelClass.getConstructor(String::class.java)
                    if (constructor != null) {
                        return constructor.newInstance(goodsId)
                    }
                } catch (exception: Exception) {

                }
                return super.create(modelClass)
            }
        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                DetailViewModel::class.java
            )
        }
    }


    fun queryDetailData(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!)
                .enqueue(object : HiCallBack<DetailModel> {
                    override fun onSuccess(response: HiResponse<DetailModel>) {
                        if (response.data != null && response.successful()) {
                            pageData.postValue(response.data)
                        } else {
                            pageData.postValue(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        pageData.postValue(null)
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace()
                        }
                    }

                })
        }

        return pageData
    }


    fun toggleFavorite(): LiveData<Boolean?> {
        val toggleFavoriteData = MutableLiveData<Boolean?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(FavoriteApi::class.java).favorite(goodsId!!)
                .enqueue(object : HiCallBack<Favorite> {
                    override fun onSuccess(response: HiResponse<Favorite>) {
                        if (response.data != null && response.successful()) {
                            toggleFavoriteData.postValue(response.data?.isFavorite)
                        } else {
                            toggleFavoriteData.postValue(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        toggleFavoriteData.postValue(null)
                    }

                })
        }
        return toggleFavoriteData
    }
}