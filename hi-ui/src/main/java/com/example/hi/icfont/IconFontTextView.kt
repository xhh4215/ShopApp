package com.example.hi.icfont

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/***
 * @author 栾桂明
 * @desc 使用IconFont资源的TextView
 * @date 2022年1月3日
 */
class IconFontTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {

    init {
        val typeface = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
        setTypeface(typeface)
    }
}