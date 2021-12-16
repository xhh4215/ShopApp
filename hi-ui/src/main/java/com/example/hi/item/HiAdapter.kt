package com.example.hi.item

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType

/****
 * 加载不好hiItem的数据列表的时候使用的adapter
 */
class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null

    /***
     * 布局加载的对象
     */
    private var mInflater: LayoutInflater? = null

    /***
     * 加载列表对应的数据
     */
    private var dataSets = ArrayList<HiDataItem<*, RecyclerView.ViewHolder>>()

    private var typeArrays = SparseArray<HiDataItem<*, RecyclerView.ViewHolder>>()


    init {
        this.mContext = context
        this.mInflater = LayoutInflater.from(context)
    }

    /***
     * 向列表的指定位置添加item
     */
    fun addItem(index: Int, item: HiDataItem<*, RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }
        //计算刷新的数据的位置
        val notifyPos = if (index > 0) index else dataSets.size - 1
        //插入的同时刷新数据
        if (notify) {
            notifyItemInserted(notifyPos)
        }

    }

    /***
     * 向列表中添加多个items
     */
    fun addItems(items: List<HiDataItem<*, RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        for (item in items) {
            dataSets.add(item)
        }
        if (notify) {
            notifyItemRangeInserted(start, items.size)
        }
    }

    /***
     * 删除列表的item元素 通过index
     */
    fun removeItem(index: Int): HiDataItem<*, RecyclerView.ViewHolder>? {
        if (index > 0 && index < dataSets.size) {
            val remove = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return remove
        }
        return null
    }

    fun removeItem(dataItem: HiDataItem<*, *>) {
        if (dataItem != null) {
            val index = dataSets.indexOf(dataItem)
            removeItem(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataItem = typeArrays.get(viewType)
        var view: View? = dataItem.getItemView(parent)
        if (view == null) {
            val layoutRes = dataItem.getItemLayoutRes()
            if (layoutRes < 0) {
                RuntimeException("dataItem:${dataItem.javaClass.name} must be overried  getItemView or getItemLayoutRes")
            }
            view = mInflater!!.inflate(dataItem.getItemLayoutRes(), parent, false)
        }

        return createViewHolderInternal(dataItem.javaClass, view)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, RecyclerView.ViewHolder>>,
        view: View?
    ): RecyclerView.ViewHolder {
        val superclass = javaClass.genericSuperclass
//        是否是参数范型类型
        if (superclass is ParameterizedType) {
//            获取范型参数的集合
            val arguments = superclass.actualTypeArguments
            for (argument in arguments) {
                if (argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(
                        argument
                    )
                ) {
                    return argument.getConstructor(View::class.java)
                        .newInstance(view) as RecyclerView.ViewHolder
                }
            }
        }

        return object : RecyclerView.ViewHolder(view!!) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hiDataItem = dataSets[position]
        hiDataItem.onBindData(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        val dataItem = dataSets[position]
        val type = dataItem.javaClass.hashCode()
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, dataItem)
        }
        return type
    }

    override fun getItemCount(): Int {
        return dataSets.size
    }

    /****
     * adapter 和recyclerview相关连时候调用
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < dataSets.size) {
                        val hiDataItem = dataSets[position]
                        val spanSize = hiDataItem.setSpanSize()
                        if (spanSize < 0) spanCount else spanSize
                    }
                    return spanCount
                }

            }
        }

    }

    fun refreshItem(hiDataItem: HiDataItem<*, *>) {
        val index = dataSets.indexOf(hiDataItem)
        notifyItemInserted(index)

    }


}