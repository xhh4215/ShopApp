package com.example.biz_login

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_login.api.AccountApi
import com.example.biz_login.databinding.ActivityLoginBinding
import com.example.common.http.ApiFactory
import com.example.common.route.HiRoute
import com.example.common.ui.component.HiBaseActivity
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.utils.HiStatusBar

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity<ActivityLoginBinding>() {
    companion object {
        private const val REQUEST_CODE_REGISTRATION = 1000
    }

    override fun layoutIdRes(): Int {
        return R.layout.activity_login
    }


    override fun initData() {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        dataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
        dataBinding.actionRegister.setOnClickListener {
            navigationRegister()
        }
        dataBinding.actionLogin.setOnClickListener {
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
        val name = dataBinding.inputItemLoginUsername.getEditText().text
        val password = dataBinding.inputItemPassword.getEditText().text
        if (TextUtils.isEmpty(name) or TextUtils.isEmpty(password)) {
            return
        }
        ApiFactory.create(AccountApi::class.java).login(name.toString(), password.toString())
            .enqueue(
                object : HiCallBack<String> {
                    override fun onSuccess(response: HiResponse<String>) {
                        if (response.code == HiResponse.SUCCESS) {
                            showToast("????????????")
                            val data = response.data
                            AccountManager.loginSuccess(data!!)
                            setResult(RESULT_OK, Intent())
                            finish()
                        } else {
                            showToast("???????????????${response.msg}")
                            Log.e("error_code", "${response.code}")
                        }
                    }

                    override fun onFailed(throwable: Throwable) {

                        showToast("???????????????${throwable.message}")
                    }

                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) and (data != null) and (requestCode == REQUEST_CODE_REGISTRATION)) {
            val username = data!!.getStringExtra("username")
            if (!TextUtils.isEmpty(username)) {
                dataBinding.inputItemLoginUsername.getEditText().setText(username)
            }
        }
    }
}