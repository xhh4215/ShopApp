package com.example.biz_detail.items

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import android.widget.TextView
import com.example.biz_detail.R
import com.example.common.ext.loadUrl
import com.example.hi.banner.core.HIBanner
import com.example.hi.banner.core.HiBannerMo
import com.example.hi.banner.indicator.HiNumIndicator
import com.example.hi.item.HiDataItem
import com.example.hi.item.HiViewHolder
import com.example.pub_mod.model.SliderImage

class HeaderItem(
    private val silderImages: List<SliderImage>?,
    private val price: String?,
    private val completeNumRText: String?,
    private val goodName: String?
) : HiDataItem<Any, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val bannerItems = arrayListOf<HiBannerMo>()
        silderImages?.forEach {
            val bannerMo = object : HiBannerMo() {}
            bannerMo.url = it.url
            bannerItems.add(bannerMo)
        }
        holder.findViewById<HIBanner>(R.id.hi_banner)!!.setHiIndicator(HiNumIndicator(context))
        holder.findViewById<HIBanner>(R.id.hi_banner)!!.setBannerData(bannerItems)
        holder.findViewById<HIBanner>(R.id.hi_banner)!!.setBindAdapter { viewHolder, mo, position ->
            val imageView = viewHolder?.rootView as? ImageView
            mo?.let {
                imageView?.loadUrl(it.url)
            }
        }
        holder.findViewById<TextView>(R.id.price)!!.text = spanPrice(price)
        holder.findViewById<TextView>(R.id.sale_desc)!!.text = completeNumRText
        holder.findViewById<TextView>(R.id.title)!!.text = goodName
    }


    private fun spanPrice(price: String?): CharSequence {
        if (TextUtils.isEmpty(price)) return ""
        val ss = SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18, true), 1, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_header
    }
}