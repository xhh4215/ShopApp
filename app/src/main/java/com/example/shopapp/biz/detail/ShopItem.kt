package com.example.shopapp.biz.detail

import android.content.Context
import android.media.Image
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.ui.view.loadUrl
import com.example.hi.item.HiAdapter
import com.example.hi.item.HiDataItem
import com.example.hi.item.HiViewHolder
import com.example.shopapp.R
import com.example.shopapp.fragment.home.GoodItem
import com.example.shopapp.model.DetailModel
import com.example.shopapp.model.GoodsModel
import com.example.shopapp.model.Shop

/**
 * 店铺模块= 基础信息+ 关联商品
 */
class ShopItem(val detailModel: DetailModel) : HiDataItem<DetailModel, HiViewHolder>() {
    private val SHOP_GOODS_ITEM_SPAN_COUNT = 3
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val shop: Shop? = detailModel.shop
        shop?.let {
            holder.findViewById<ImageView>(R.id.shop_logo)!!.loadUrl(it.logo)
            holder.findViewById<TextView>(R.id.shop_title)!!.text = it.name
            holder.findViewById<TextView>(R.id.shop_desc)!!.text =
                String.format(context.getString(R.string.shop_desc), it.goodsNum, it.completedNum)

            val evaluation: String? = shop.evaluation
            evaluation?.let {
                val tagContainer = holder.findViewById<LinearLayout>(R.id.tag_container)
                tagContainer!!.visibility = View.VISIBLE
                //6个元素 每2个组成一个标签  "evaluation":"描述相符 高 服务态度 高 物流服务 高"
                val serviceTags = evaluation.split(" ")
                var index = 0
                for (tagIndex in 0..(serviceTags.size / 2 - 1)) {//6 3 in 0..3
                    val tagView: TextView = if (tagIndex < tagContainer.childCount) {
                        tagContainer.getChildAt(tagIndex) as TextView
                    } else {
                        val tagView = TextView(context)
                        val params =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        params.weight = 1f
                        tagView.layoutParams = params
                        tagView.gravity = Gravity.CENTER
                        tagView.textSize = 14f
                        tagView.setTextColor(ContextCompat.getColor(context, R.color.color_666))
                        tagContainer.addView(tagView)

                        tagView
                    }

                    val serviceName =
                        if (index >= serviceTags.size) continue else serviceTags[index]
                    val serviceTag = serviceTags[index + 1]
                    index += 2

                    val spanTag = spanServiceTag(context, serviceName, serviceTag)
                    tagView.text = spanTag
                }

            }
        }


        val flowGoods: List<GoodsModel>? = detailModel.flowGoods
        flowGoods?.let {
            val flowRecyclerView: RecyclerView =
                holder.findViewById(R.id.flow_recycler_view)!!
            flowRecyclerView.visibility = View.VISIBLE

            if (flowRecyclerView.layoutManager == null) {
                flowRecyclerView.layoutManager =
                    GridLayoutManager(context, SHOP_GOODS_ITEM_SPAN_COUNT)
            }
            if (flowRecyclerView.adapter == null) {
                flowRecyclerView.adapter = HiAdapter(context)
            }

            val dataItems = mutableListOf<GoodItem>()
            it.forEach {
                dataItems.add(ShopGoodsItem(it))
            }
            val adapter = flowRecyclerView.adapter as HiAdapter
            adapter.clearItems()
            adapter.addItems(dataItems, true)
        }

    }

    private inner class ShopGoodsItem(goodsModel: GoodsModel) : GoodItem(goodsModel, false) {
        override fun getItemLayoutRes(): Int {
            return R.layout.layout_detail_item_shop_goods_item
        }

        override fun onViewAttachedToWindow(holder: GoodItemHolder) {
            super.onViewAttachedToWindow(holder)
            val viewParent: ViewGroup = holder.itemView.parent as ViewGroup
            val availableWidth =
                viewParent.measuredWidth - viewParent.paddingLeft - viewParent.paddingRight
            val itemWidth = availableWidth / SHOP_GOODS_ITEM_SPAN_COUNT

            val itemImage = holder.findViewById<ImageView>(R.id.item_image)
            val params = itemImage!!.layoutParams
            params.width = itemWidth
            params.height = itemWidth
            itemImage.layoutParams = params
        }


    }

    private fun spanServiceTag(
        context: Context,
        serviceName: String,
        serviceTag: String
    ): CharSequence {
        val ss = SpannableString(serviceTag)
        val ssb = SpannableStringBuilder()

        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_c61)),
            0,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            BackgroundColorSpan(ContextCompat.getColor(context, R.color.color_f8e)),
            0,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.append(serviceName)
        ssb.append(ss)
        return ssb
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_shop
    }
}