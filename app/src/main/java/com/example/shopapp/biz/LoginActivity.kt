package com.example.shopapp.biz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.ui.component.HiBaseActivity
import com.example.common.utils.SPUtil
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.utils.HiStatusBar
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityLoginBinding
import com.example.shopapp.http.api.AccountApi
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.route.HiRoute

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity() {
    companion object {
        private const val REQUEST_CODE_REGISTRATION = 1000
    }

    private lateinit var loginDataBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        loginDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginDataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
        loginDataBinding.actionRegister.setOnClickListener {
            navigationRegister()
        }
        loginDataBinding.actionLogin.setOnClickListener {
            goLogin()
        }
    }

    private fun navigationRegister() {
        HiRoute.startActivity(
            this,
            destination = HiRoute.Destination.ACCOUNT_REGISTRATION,
            requestCode = REQUEST_CODE_REGISTRATION
        )
    }

    private fun goLogin() {
        val name = loginDataBinding.inputItemLoginUsername.getEditText().text
        val password = loginDataBinding.inputItemPassword.getEditText().text
        if (TextUtils.isEmpty(name) or TextUtils.isEmpty(password)) {
            return
        }
        ApiFactory.create(AccountApi::class.java).login(name.toString(), password.toString())
            .enqueue(
                object : HiCallBack<String> {
                    override fun onSuccess(response: HiResponse<String>) {
                        if (response.code == HiResponse.SUCCESS) {
                            showToast("登录成功")
                            val data = response.data
                            SPUtil.putString("boarding-pass", data!!)
                            setResult(RESULT_OK, Intent())
                            finish()
                        } else {
                            showToast("登录失败：${response.msg}")
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        showToast("登录失败：${throwable.message}")
                    }

                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data != null) and (requestCode == REQUEST_CODE_REGISTRATION)) {
            val username = data!!.getStringExtra("username")
            if (!TextUtils.isEmpty(username)) {
                loginDataBinding.inputItemLoginUsername.getEditText().setText(username)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    }
}