package com.example.shopapp.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.common.ui.component.HiBaseFragment
import com.example.common.ui.view.loadCircleUrl
import com.example.common.ui.view.loadCorner
import com.example.hi.banner.core.HIBanner
import com.example.hi.banner.core.HiBannerMo
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.utils.HiDisplayUtil
import com.example.shopapp.R
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.AccountApi
import com.example.shopapp.model.CourseNotice
import com.example.shopapp.model.Notice
import com.example.shopapp.model.UserProfile
import com.example.shopapp.route.HiRoute

class ProfileFragment : HiBaseFragment() {
    private val REQUEST_CODE_LOGIN_PROFILE = 1001
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryLoginUserData()
    }

    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object : HiCallBack<CourseNotice> {
                override fun onSuccess(response: HiResponse<CourseNotice>) {
                    if (response.code == HiResponse.SUCCESS && response.data!!.total > 0) {
                        val noticeCount = layoutView.findViewById<TextView>(R.id.notify_count)
                        noticeCount.text = response.data!!.total.toString()
                        noticeCount.visibility = View.VISIBLE
                    }
                }

                override fun onFailed(throwable: Throwable) {

                }

            })
    }

    private fun queryLoginUserData() {
        ApiFactory.create(AccountApi::class.java).profile().enqueue(object :
            HiCallBack<UserProfile> {
            override fun onSuccess(response: HiResponse<UserProfile>) {
                val profile = response.data
                if (response.code == HiResponse.SUCCESS && profile != null) {
                    queryCourseNotice()
                    //请求成功处理UI
                    updateUI(profile)
                } else {
                    showToast("${response.msg}")
                }
            }

            override fun onFailed(throwable: Throwable) {
                showToast("${throwable.message}")
            }
        })
    }

    private fun updateUI(profile: UserProfile) {
        val userName = layoutView.findViewById<TextView>(R.id.user_name)
        userName.text =
            if (profile.isLogin) profile.userName else getString(R.string.profile_not_login)
        val loginDesc = layoutView.findViewById<TextView>(R.id.login_desc)
        loginDesc.text =
            if (profile.isLogin) getString(R.string.profile_welcome_back) else getString(
                R.string.profile_relock_all
            )
        val userAvatar = layoutView.findViewById<ImageView>(R.id.user_avatar)
        if (profile.isLogin && profile.userIcon != null) {
            userAvatar.loadCircleUrl(profile.userIcon)
        } else {
            userAvatar.setImageResource(R.drawable.ic_avatar_default)
            userName.setOnClickListener {
                HiRoute.startActivity(
                    context,
                    destination = HiRoute.Destination.ACCOUNT_LOGIN,
                    requestCode = REQUEST_CODE_LOGIN_PROFILE
                )
            }
        }
        val itemCollection = layoutView.findViewById<TextView>(R.id.tab_item_collection)
        val itemHistory = layoutView.findViewById<TextView>(R.id.tab_item_history)
        val itemLearn = layoutView.findViewById<TextView>(R.id.tab_item_learn)
        itemCollection.text =
            spannableTabItem(profile.favoriteCount, getString(R.string.profile_collection))
        itemHistory.text =
            spannableTabItem(profile.favoriteCount, getString(R.string.profile_brow_history))
        itemLearn.text =
            spannableTabItem(profile.favoriteCount, getString(R.string.profile_study_time))
        updateBanner(profile.bannerNoticeList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN_PROFILE && resultCode == Activity.RESULT_OK && data != null) {
            queryLoginUserData()
        }
    }

    private fun updateBanner(bannerList: List<Notice>?) {
        val banner = layoutView.findViewById<HIBanner>(R.id.hi_banner)
        var modules = mutableListOf<HiBannerMo>()
        if (bannerList == null || bannerList.isEmpty()) return
        bannerList.forEach {
            var bannerMo = object : HiBannerMo() {}
            bannerMo.url = it.cover
            modules.add(bannerMo)
        }
        banner.setBannerData(R.layout.layout_file_banner_item, modules)
        banner.setBindAdapter { viewHolder, mo, position ->
            if (viewHolder == null || mo == null) return@setBindAdapter
            val imageView = viewHolder.findViewById<ImageView>(R.id.banner_item_image)
            imageView.loadCorner(mo.url, HiDisplayUtil.dp2px(10f, resources))
        }
        banner.setOnBannerClickListener { viewHolder, hiBannerMo, position ->
            HiRoute.startActivity4Browser(bannerList[position].url)
        }
        banner.visibility = View.VISIBLE
    }

    private fun spannableTabItem(favoriteCount: Int, bottomText: String): CharSequence? {
        var spanStr = favoriteCount.toString()
        val builder = SpannableStringBuilder()
        val span = SpannableString(spanStr)
        span.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.color_000)),
            0,
            span.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(AbsoluteSizeSpan(18, true), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(span)
        builder.append(bottomText)
        return builder.toString()
    }

    private fun showToast(message: String) {
        if (message == null) return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}