package com.example.biz_home.home

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
 import com.example.hi.banner.core.HIBanner
import com.example.hi.banner.core.HiBannerMo
import com.example.hi.item.HiDataItem
import com.example.library.utils.HiDisplayUtil
import com.example.biz_home.model.HomeBanner
import com.example.common.ext.loadUrl
import com.example.common.route.HiRoute

class BannerItem(val list: List<HomeBanner>) :
    HiDataItem<List<HomeBanner>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val banner = holder.itemView as HIBanner
        val models = mutableListOf<HiBannerMo>()
        list.forEachIndexed { _, homeBanner ->
            val bannerMo = object : HiBannerMo() {}
            bannerMo.url = homeBanner.cover
            models.add(bannerMo)
        }
        banner.setBannerData(models)
        banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            val homeBanner = list[position]
            if (TextUtils.equals(homeBanner.type, HomeBanner.TYPE_GOODS)) {
                //跳详情页....
                Toast.makeText(context, "you  touch me:$position", Toast.LENGTH_SHORT).show()
            } else {
                HiRoute.startActivity4Browser(homeBanner.url)
            }

        }
        banner.setBindAdapter { viewHolder, mo, _ ->
            ((viewHolder.rootView) as ImageView).loadUrl(mo.url)
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val context = parent.context
        val banner = HIBanner(context)
        val layoutParam = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            HiDisplayUtil.dp2px(160f, context.resources)
        )
        layoutParam.bottomMargin = HiDisplayUtil.dp2px(10f, context.resources)
        banner.layoutParams = layoutParam
        banner.setBackgroundColor(Color.WHITE)
        return banner

    }
}