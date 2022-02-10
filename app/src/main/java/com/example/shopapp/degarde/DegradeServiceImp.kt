package com.example.shopapp.degarde

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import com.example.common.route.HiRoute

@Route(path = "/degrade/global/service")
class DegradeServiceImp : DegradeService {
    override fun init(context: Context?) {
        HiRoute.startActivity(context, destination = HiRoute.Destination.DEGRADE_GLOBAL)
    }
    override fun onLost(context: Context?, postcard: Postcard?) {
     }
}