package com.example.biz_home.notice

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_home.R
import com.example.biz_home.api.AccountApi
import com.example.biz_home.databinding.ActivityNoticeListBinding
import com.example.biz_home.model.CourseNotice
import com.example.common.http.ApiFactory
import com.example.common.ui.component.HiBaseActivity
import com.example.hi.item.HiAdapter
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.utils.HiStatusBar

@Route(path = "/notice/list")
class NoticeListActivity : HiBaseActivity<ActivityNoticeListBinding>() {
    private lateinit var adapter: HiAdapter
    private lateinit var courseNotice: CourseNotice
    override fun layoutIdRes(): Int {
        return R.layout.activity_notice_list
    }

    override fun initData() {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        initUI()
        queryCourseNotice()
    }

    private fun initUI() {
        val layoutManager = LinearLayoutManager(this)
        adapter = HiAdapter(this)
        dataBinding.list.layoutManager = layoutManager
        dataBinding.list.adapter = adapter
    }
    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object : HiCallBack<CourseNotice> {
                override fun onSuccess(response: HiResponse<CourseNotice>) {
                    response.data?.let { bindData(it) }
                }

                override fun onFailed(throwable: Throwable) {

                }
            })
    }
    private fun bindData(data: CourseNotice) {
        courseNotice = data
        data.list?.map {
            adapter.addItemAt(
                0,
                NoticeItem(it), true
            )
        }

    }


}