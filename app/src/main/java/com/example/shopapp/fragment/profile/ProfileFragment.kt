package com.example.shopapp.fragment.profile

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.common.ui.component.HiBaseFragment
import com.example.common.ui.view.loadCircleUrl
import com.example.common.ui.view.loadCorner
import com.example.hi.banner.core.HIBanner
import com.example.hi.banner.core.HiBannerMo
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.utils.HiDisplayUtil
import com.example.shopapp.R
import com.example.shopapp.biz.account.AccountManager
import com.example.shopapp.databinding.FragmentProfileBinding
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.AccountApi
import com.example.shopapp.model.CourseNotice
import com.example.shopapp.model.Notice
import com.example.shopapp.model.UserProfile
import com.example.shopapp.route.HiRoute

class ProfileFragment : HiBaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryLoginUserData()
        queryCourseNotice()
    }

    private fun queryCourseNotice() {
        viewModel.queryCourseNotice().observe(viewLifecycleOwner, Observer {
            dataBinding.courseNotice = it
        })

    }

    private fun queryLoginUserData() {
        AccountManager.getUserProfile(this, observer = Observer { profile ->
            if (profile != null) {
                dataBinding.userProfile = profile
                updateUI(profile)
            } else {
                showToast("获取用户信息失败")
            }

        }, false)
    }

    private fun updateUI(profile: UserProfile) {
        if (!profile.isLogin) {
            dataBinding.userAvatar.setImageResource(R.drawable.ic_avatar_default)
            dataBinding.userAvatar.setOnClickListener {
                AccountManager.login(context, observer = Observer {
                    queryLoginUserData()
                })
            }
        }
        updateBanner(profile.bannerNoticeList)
        dataBinding.llNotice.setOnClickListener {
            HiRoute.startActivity(context, destination = HiRoute.Destination.NOTICE_LIST)
        }
    }

    private fun updateBanner(bannerList: List<Notice>?) {
        var modules = mutableListOf<HiBannerMo>()
        if (bannerList == null || bannerList.isEmpty()) return
        bannerList.forEach {
            var bannerMo = object : HiBannerMo() {}
            bannerMo.url = it.cover
            modules.add(bannerMo)
        }
        dataBinding.hiBanner.setBannerData(R.layout.layout_file_banner_item, modules)
        dataBinding.hiBanner.setBindAdapter { viewHolder, mo, position ->
            if (viewHolder == null || mo == null) return@setBindAdapter
            val imageView = viewHolder.findViewById<ImageView>(R.id.banner_item_image)
            imageView.loadCorner(mo.url, HiDisplayUtil.dp2px(10f, resources))
        }
        dataBinding.hiBanner.setOnBannerClickListener { viewHolder, hiBannerMo, position ->
            HiRoute.startActivity4Browser(bannerList[position].url)
        }
        dataBinding.hiBanner.visibility = View.VISIBLE
    }


    private fun showToast(message: String) {
        if (message == null) return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}