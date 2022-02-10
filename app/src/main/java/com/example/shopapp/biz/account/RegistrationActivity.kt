package com.example.shopapp.biz.account

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.ui.component.HiBaseActivity
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.utils.HiStatusBar
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityRegistBinding
import com.example.shopapp.http.api.AccountApi
import com.example.shopapp.http.ApiFactory

@Route(path = "/account/registration")
class RegistrationActivity : HiBaseActivity<ActivityRegistBinding>() {
    override fun initData() {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        dataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
        dataBinding.actionSubmit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val orderId = dataBinding.inputItemOrderId.getEditText().text.toString()
        val moocId = dataBinding.inputItemMoocId.getEditText().text.toString()
        val username = dataBinding.inputItemUsername.getEditText().text.toString()
        val pwd = dataBinding.inputItemPwd.getEditText().text.toString()
        val pwdSec = dataBinding.inputItemPwdCheck.getEditText().text.toString()
        if (TextUtils.isEmpty(orderId)
            || (TextUtils.isEmpty(moocId))
            || (TextUtils.isEmpty(username))
            || TextUtils.isEmpty(pwd)
            || (!TextUtils.equals(pwd, pwdSec))
        ) {
            return
        }
        ApiFactory.create(AccountApi::class.java).register(username, pwd, moocId, orderId)
            .enqueue(object : HiCallBack<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.code == HiResponse.SUCCESS) {
                        //注册成功
                        var intent = Intent()
                        intent.putExtra("username", username)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        response.msg?.let { showToast(it) }
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    throwable.message?.let { showToast(it) }
                }
            })


    }


    override fun layoutIdRes(): Int {
        return R.layout.activity_regist
    }
}