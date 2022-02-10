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
import com.example.library.utils.MainHandler
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.AccountApi
import com.example.shopapp.model.UserProfile
import java.lang.IllegalStateException

object AccountManager {
    private val lock = Any()
    private var profile: UserProfile? = null
    private var boardingPass: String? = null
    private const val KEY_BOARDING_PASS = "key_boarding_pass"
    private const val KEY_USER_PROFILE = "key_user_profile"
    private val loginLiveData = ObserverLiveData<Boolean>()

    private val profileLiveData = ObserverLiveData<UserProfile?>()
    @Volatile
    private var isFetching = false
    init {
        HiExecutor.execute(runnable = Runnable {
            val local = HiStorage.getCache<UserProfile?>(KEY_USER_PROFILE)
            synchronized(lock) {
                if (profile == null && local == null) {
                    profile = local
                }
            }
        })
    }

    /***
     * @param observer 对登录的结果进行观察和处理
     */
    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
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
        onlyCache: Boolean = false
    ) {
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }
        if (isFetching) return
        isFetching = true

        if (onlyCache) {
            synchronized(lock) {
                if (profile != null) {
                    MainHandler.post(Runnable {
                        this.profileLiveData.value = profile
                    })
                    return
                }
            }
        }
        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallBack<UserProfile> {
              override fun onSuccess(response: HiResponse<UserProfile>) {
                if (response.successful() && response.data != null) {
                    profile = response.data
                    HiExecutor.execute(runnable = Runnable {
                        HiStorage.saveCache(KEY_USER_PROFILE, profile)
                        isFetching = false
                    })
                    profileLiveData.value = profile
                } else {
                    profileLiveData.value = null
                }
            }


            override fun onFailed(throwable: Throwable) {
                isFetching = false
                profileLiveData.value = null
            }
        })
    }

    private class ObserverLiveData<T> : MutableLiveData<T>() {
        private val observers = arrayListOf<Observer<in T>>()
        override fun observeForever(observer: Observer<in T>) {
            super.observeForever(observer)
            if (!observers.contains(observer)) {
                observers.add(observer)
            }
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            if (!observers.contains(observer)) {
                observers.add(observer)
            }
        }

        fun removeAllObservers() {
            for (observer in observers) {
                removeObserver(observer)
            }
            observers.clear()
        }
    }

    //主动清除observers防止 foreverObserver 释放不掉,
    //同时也能规避 多次打开登录页，多次注册observer的问题
    internal fun clearObservers() {
        loginLiveData.removeAllObservers()
        profileLiveData.removeAllObservers()
    }
}