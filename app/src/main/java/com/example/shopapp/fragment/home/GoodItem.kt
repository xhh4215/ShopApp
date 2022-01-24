package com.example.shopapp.fragment.home

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.common.ui.view.loadUrl
import com.example.hi.item.HiDataItem
import com.example.library.utils.HiDisplayUtil
import com.example.shopapp.R
import com.example.shopapp.model.GoodsModel

class GoodItem(private val goodsModel: GoodsModel, val hotTab: Boolean) :
    HiDataItem<GoodsModel, RecyclerView.ViewHolder>(goodsModel) {
    val MAX_TAG_SIZE = 3
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.itemView.findViewById<ImageView>(R.id.item_image).loadUrl(goodsModel.sliderImage)
        holder.itemView.findViewById<TextView>(R.id.item_title).text = goodsModel.goodsName
        holder.itemView.findViewById<TextView>(R.id.item_price).text = goodsModel.marketPrice
        holder.itemView.findViewById<TextView>(R.id.item_count).text = goodsModel.completedNumText
        holder.itemView.findViewById<TextView>(R.id.item_count).text = goodsModel.completedNumText
        val labelContainer = holder.itemView.findViewById<LinearLayout>(R.id.item_label_container)
        if (!TextUtils.isEmpty(goodsModel.tags)) {
            labelContainer.visibility = View.VISIBLE
            val splits = goodsModel.tags.split(" ")
            for (index in splits.indices) {
                val childCount = labelContainer.childCount
                if (index > MAX_TAG_SIZE - 1) {
                    for (index in childCount - 1 downTo MAX_TAG_SIZE - 1) {
                        labelContainer.removeViewAt(index)
                    }
                    break
                }
                val labelView: TextView = if (index > labelContainer.childCount - 1) {
                    val view = createLabelView(context, index != 0)
                    labelContainer.addView(view)
                    view
                } else {
                    labelContainer.getChildAt(index) as TextView
                }

                labelView.text = splits[index]
            }

        } else {
            labelContainer.visibility = View.GONE
        }

        if (!hotTab) {
            val margin = HiDisplayUtil.dp2px(2f)
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = hiAdapter?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = hiAdapter?.getAttachRecyclerView()?.paddingLeft ?: 0
            val itemLeft = holder.itemView.left
            if (itemLeft == (parentLeft + parentPaddingLeft)) {
                layoutParams.rightMargin = margin
            } else {
                layoutParams.leftMargin = margin
            }
            holder.itemView.layoutParams = layoutParams
        }
    }


    private fun createLabelView(context: Context, withLeftMargin: Boolean): TextView {
        val labelTextView = TextView(context)
        labelTextView.setTextColor(ContextCompat.getColor(context, R.color.color_eed))
        labelTextView.textSize = 11f
        labelTextView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            HiDisplayUtil.dp2px(14f)
        )
        params.leftMargin = if (withLeftMargin) HiDisplayUtil.dp2px(5f) else 0
        labelTextView.layoutParams = params
        return labelTextView
    }

    override fun getItemLayoutRes(): Int {
        return if (hotTab) R.layout.layout_home_good_list_item1 else R.layout.layout_home_good_list_item2
    }

    override fun getSpanSize(): Int {
        return if (hotTab) super.getSpanSize() else 1
    }
}