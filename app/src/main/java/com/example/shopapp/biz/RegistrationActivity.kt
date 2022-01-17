package com.example.shopapp.biz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
class RegistrationActivity : HiBaseActivity() {
    private lateinit var registerDataBinding: ActivityRegistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        registerDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_regist)
        registerDataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
        registerDataBinding.actionSubmit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val orderId = registerDataBinding.inputItemOrderId.getEditText().text.toString()
        val moocId = registerDataBinding.inputItemMoocId.getEditText().text.toString()
        val username = registerDataBinding.inputItemUsername.getEditText().text.toString()
        val pwd = registerDataBinding.inputItemPwd.getEditText().text.toString()
        val pwdSec = registerDataBinding.inputItemPwdCheck.getEditText().text.toString()
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

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    }
}