package com.example.hi.slider

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Layout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hi.R
import com.example.hi.item.HiViewHolder

/****
 * @author 栾桂明
 * @date 2022年 1月
 * @desc 侧面的分类菜单
 */
class HiSliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    //侧面的分类的item的列表
    private val MENU_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_menu_item

    //内容的列表
    private val CONTENT_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_content_item

    private val menuView = RecyclerView(context)

    val contentView = RecyclerView(context)

    //一个保存自定义属性的字段
    private var menuItemAttr = AttrParse.parseMenuItemAttr(context, attrs)

    /****
     * 做的是侧面分类自定义视图的基本设置操作
     */
    init {
        orientation = HORIZONTAL
        menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        menuView.overScrollMode = View.OVER_SCROLL_NEVER
        menuView.itemAnimator = null
        contentView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.overScrollMode = View.OVER_SCROLL_NEVER
        contentView.itemAnimator = null
        addView(menuView)
        addView(contentView)
    }

    /****
     * 绑定内容的菜单
     */
    fun bindMenuView(
        layoutRes: Int = MENU_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {
        menuView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        menuView.adapter = MenuAdapter(layoutRes, itemCount, onBindView, onItemClick)
    }

    fun bindContentView(
        layoutRes: Int = CONTENT_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        itemDecoration: RecyclerView.ItemDecoration?,
        layoutManager: RecyclerView.LayoutManager,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {
        if (contentView.layoutManager == null) {
            contentView.layoutManager = layoutManager
            contentView.adapter = ContentAdapter(layoutRes)
            itemDecoration?.let {
                contentView.addItemDecoration(it)
            }
        }
        val contentAdapter = contentView.adapter as ContentAdapter
        contentAdapter.update(itemCount, onBindView, onItemClick)
        contentAdapter.notifyDataSetChanged()
        contentView.scrollToPosition(0)
    }

    inner class ContentAdapter(val layoutRes: Int) : RecyclerView.Adapter<HiViewHolder>() {
        private lateinit var onItemClick: (HiViewHolder, Int) -> Unit
        private lateinit var onBindView: (HiViewHolder, Int) -> Unit
        private var count: Int = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            return HiViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return count
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            onBindView(holder, position)
            holder.itemView.setOnClickListener {
                onItemClick(holder, position)
            }
        }

        override fun onViewAttachedToWindow(holder: HiViewHolder) {
            super.onViewAttachedToWindow(holder)
            val remainSpace = width - paddingLeft - paddingRight - menuItemAttr.width
            val layoutManager = contentView.layoutManager
            var spanCount = 0
            if (layoutManager is GridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                spanCount = layoutManager.spanCount
            }

            if (spanCount > 0) {
                val itemWidth = remainSpace / spanCount
                //创建content itemview  ，设置它的layoutparams 的原因，是防止图片未加载出来之前，列表滑动时 上下闪动的效果
                val layoutParams = holder.itemView.layoutParams
                layoutParams.width = itemWidth
                layoutParams.height = itemWidth
                holder.itemView.layoutParams = layoutParams
            }
        }

        fun update(
            itemCount: Int,
            onBindView: (HiViewHolder, Int) -> Unit,
            onItemClick: (HiViewHolder, Int) -> Unit
        ) {
            this.count = itemCount
            this.onBindView = onBindView
            this.onItemClick = onItemClick
        }

    }


    inner class MenuAdapter(
        private val layoutRes: Int,
        val count: Int,
        val onBindView: (HiViewHolder, Int) -> Unit,
        val onItemClick: (HiViewHolder, Int) -> Unit
    ) : RecyclerView.Adapter<HiViewHolder>() {
        //本次选中的位置
        private var currentSelectIndex = 0

        //上次选中的位置
        private var lastSelectIndex = 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            //加载菜单使用的布局文件
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            //通过layoutParams设置menuView的大小
            val params = RecyclerView.LayoutParams(menuItemAttr.width, menuItemAttr.height)
            itemView.layoutParams = params
            //设置背景颜色
            itemView.setBackgroundColor(menuItemAttr.normalBackgroundColor)
            //设置内部的标题的文本颜色
            itemView.findViewById<TextView>(R.id.menu_item_title)
                ?.setTextColor(menuItemAttr.textColor)
            //设置内部的指示器的形状
            itemView.findViewById<ImageView>(R.id.menu_item_indicator)
                ?.setImageDrawable(menuItemAttr.indicator)
            return HiViewHolder(itemView)
        }

        /****
         * 菜单内容的绑定操作
         */
        override fun onBindViewHolder(
            holder: HiViewHolder,
            @SuppressLint("RecyclerView") position: Int
        ) {
            //
            holder.itemView.setOnClickListener {
                //每次点击将点击的位置设置为当前选中的位置
                currentSelectIndex = position
                notifyItemChanged(position)
                notifyItemChanged(lastSelectIndex)
            }
            if (currentSelectIndex == position) {
                onItemClick(holder, position)
                lastSelectIndex = currentSelectIndex
            }
            applyItemAttr(position, holder)
            onBindView(holder, position)
        }

        private fun applyItemAttr(position: Int, holder: RecyclerView.ViewHolder) {
            val selected = position == currentSelectIndex
            val titleView: TextView? = holder.itemView.findViewById(R.id.menu_item_title)
            val indicatorView: ImageView? =
                holder.itemView.findViewById(R.id.menu_item_indicator)
            indicatorView?.visibility = if (selected) View.VISIBLE else View.GONE
            titleView?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                if (selected) menuItemAttr.selectTextSize.toFloat() else menuItemAttr.textSize.toFloat()
            )
            holder.itemView.setBackgroundColor(if (selected) menuItemAttr.selectBackgroundColor else menuItemAttr.normalBackgroundColor)
            titleView?.isSelected = selected
        }

        override fun getItemCount(): Int {
            return count
        }

    }


}


