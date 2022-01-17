package com.example.common.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.example.common.R

class EmptyView : LinearLayout {
    private var icon: IconFontTextView
    private var title: TextView
    private var desc: TextView
    private var button: Button

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int = 0) : super(
        context,
        attributeSet,
        defStyle
    ) {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)
        icon = findViewById(R.id.empty_icon)
        title = findViewById(R.id.empty_title)
        desc = findViewById(R.id.empty_text)
        button = findViewById(R.id.empty_action)
    }

    fun setIcon(@StringRes iconResult: Int) {
        icon.setText(iconResult)
    }

    fun setDesc(text: String) {
        title!!.text = text
        title!!.visibility == if (TextUtils.isDigitsOnly(text)) View.GONE else View.VISIBLE
    }

    fun setTitle(text: String) {
        title.text = text
        title!!.visibility == if (TextUtils.isDigitsOnly(text)) View.GONE else View.VISIBLE
    }

    @JvmOverloads
    fun setHelperAction(iconRes: Int = R.string.if_detail, listener: OnClickListener) {
        findViewById<IconFontTextView>(R.id.empty_tips).setText(iconRes)
        findViewById<IconFontTextView>(R.id.empty_tips).setOnClickListener {
            listener
        }
        findViewById<IconFontTextView>(R.id.empty_tips).visibility = View.VISIBLE
        if (iconRes == -1) {
            findViewById<IconFontTextView>(R.id.empty_tips).visibility = View.GONE
        }
    }

    fun setButton(text: String, listener: OnClickListener) {
        if (TextUtils.isEmpty(text)) {
            button.visibility = View.GONE
        } else {
            button.visibility = View.VISIBLE
            button.setOnClickListener {
                listener
            }
        }
    }
}