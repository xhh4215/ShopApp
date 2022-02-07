package com.example.shopapp.biz.account

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.common.utils.SPUtil
import com.example.library.cache.HiStorage
import com.example.library.executor.HiExecutor
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.utils.AppGlobals
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.AccountApi
import com.example.shopapp.model.UserProfile
import java.lang.IllegalStateException

object AccountManager {
    private var profile: UserProfile? = null
    private var boardingPass: String? = null
    private const val KEY_BOARDING_PASS = "key_boarding_pass"
    private const val KEY_USER_PROFILE = "key_user_profile"
    private val loginLiveData = MutableLiveData<Boolean>()

    @Volatile
    private var isFetching = false
    private val profileLiveData = MutableLiveData<UserProfile>()
    private val loginForeverObservers = mutableListOf<Observer<Boolean>>()
    private val profileForeverObservers = mutableListOf<Observer<UserProfile?>>()

    /***
     * @param observer 对登录的结果进行观察和处理
     */
    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
            loginForeverObservers.add(observer)
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            throw IllegalStateException("context must bo not null")
        }
        context.startActivity(intent)
    }


    fun loginSuccess(boardPass: String) {
        SPUtil.putString(KEY_BOARDING_PASS, boardPass)
        boardingPass = boardPass
        loginLiveData.value = true
        clearLoginForeverObservers()
    }

    private fun clearLoginForeverObservers() {
        for (observer in loginForeverObservers) {
            loginLiveData.removeObserver(observer)
            loginForeverObservers.clear()
        }
    }

    private fun clearProfileForeverObservers() {
        for (observer in profileForeverObservers) {
            profileLiveData.removeObserver(observer)
            loginForeverObservers.clear()
        }
    }

    fun getBoardingPass(): String? {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getBoardingPass())
    }

    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean
    ) {
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
            profileForeverObservers.add(observer)
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }
        if (profile != null && onlyCache) {
            profileLiveData.postValue(profile)
        }
        if (isFetching) return
        isFetching = true
        ApiFactory.create(AccountApi::class.java).profile().enqueue(object :
            HiCallBack<UserProfile> {
            override fun onSuccess(response: HiResponse<UserProfile>) {
                profile = response.data
                if (response.code == HiResponse.SUCCESS && profile != null) {
                    HiExecutor.execute(runnable = Runnable {
                        HiStorage.saveCache(KEY_USER_PROFILE, profile)
                        isFetching = false
                    })
                    profileLiveData.value = profile
                } else {
                    profileLiveData.value = null
                }
                clearProfileForeverObservers()
            }


            override fun onFailed(throwable: Throwable) {
                isFetching = false
                profileLiveData.value = null
                clearProfileForeverObservers()
            }
        })
    }
}