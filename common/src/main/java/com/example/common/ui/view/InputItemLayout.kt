package com.example.common.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.common.R

/***
 * @author 栾桂明
 * @desc 一个自定义的文本输入框
 * @date 2022年1月3日
 */
class InputItemLayout : LinearLayout {

    private lateinit var titleView: TextView
    private lateinit var editText: EditText
    private var topLine: Line
    private var bottomLine: Line

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        orientation = HORIZONTAL
        val array: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.InputItemLayout)
        // 解析输入的属性
        val titleStyleId = array.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        val title = array.getString(R.styleable.InputItemLayout_title)
        parseTitleStyle(titleStyleId, title)
        // 解析输入的属性
        val inputStyleId = array.getResourceId(R.styleable.InputItemLayout_inputTextAppearance, 0)
        val hint = array.getString(R.styleable.InputItemLayout_hint)
        val inputType = array.getInteger(R.styleable.InputItemLayout_inputType, 0)
        parseInputStyle(inputStyleId, hint, inputType)
        //解析分割线属性
        val bottomLineStyleId =
            array.getResourceId(R.styleable.InputItemLayout_bottomLineTextAppearance, 0)
        val topLineStyleId =
            array.getResourceId(R.styleable.InputItemLayout_topLineTextAppearance, 0)
        topLine = parseLineStyle(topLineStyleId)
        if (topLine.enable) {
            topPaint.color = topLine.color
            topPaint.style = Paint.Style.FILL_AND_STROKE
            topPaint.strokeWidth = topLine.height.toFloat()
        }
        bottomLine = parseLineStyle(bottomLineStyleId)
        if (bottomLine.enable) {
            bottomPaint.color = topLine.color
            bottomPaint.style = Paint.Style.FILL_AND_STROKE
            bottomPaint.strokeWidth = topLine.height.toFloat()
        }

        array.recycle()
    }

    /***
     * 解析分割线的属性
     */
    private fun parseLineStyle(resId: Int): Line {
        var line = Line()
        val array = context.obtainStyledAttributes(resId, R.styleable.lineAppearance)
        line.color =
            array.getColor(
                R.styleable.lineAppearance_color,
                ContextCompat.getColor(context, R.color.color_d1d2)
            )
        line.height =
            array.getDimensionPixelOffset(R.styleable.lineAppearance_height, 0)
        line.leftMargin = array.getDimensionPixelOffset(R.styleable.lineAppearance_leftMargin, 0)
        line.rightMargin = array.getDimensionPixelOffset(R.styleable.lineAppearance_rightMargin, 0)
        line.enable = array.getBoolean(R.styleable.lineAppearance_enable, false)
        array.recycle()
        return line
    }

    inner class Line {
        var color = 0
        var height = 0
        var leftMargin = 0
        var rightMargin = 0
        var enable = false

    }

    /***
     * 解析输入的属性
     */
    private fun parseInputStyle(resId: Int, hint: String?, inputType: Int) {
        val array = context.obtainStyledAttributes(resId, R.styleable.inputTextAppearance)
        val hintColor = array.getColor(
            R.styleable.inputTextAppearance_hintColor,
            resources.getColor(R.color.color_d1d2)
        )
        val inputColor = array.getColor(
            R.styleable.inputTextAppearance_inputColor,
            resources.getColor(R.color.color_565)
        )
        val textSize = array.getDimensionPixelSize(
            R.styleable.inputTextAppearance_textSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f)
        )
        editText = EditText(context)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        editText.layoutParams = params
        editText.hint = hint
        editText.setHintTextColor(hintColor)
        editText.setTextColor(inputColor)
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        editText.gravity = Gravity.LEFT or (Gravity.CENTER)
        when (inputType) {
            0 -> {
                editText.inputType = InputType.TYPE_CLASS_TEXT
            }
            1 -> {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or (InputType.TYPE_CLASS_TEXT)
            }
            2 -> {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
        addView(editText)
        array.recycle()
    }

    /***
     * 解析title资源属性
     */
    private fun parseTitleStyle(resId: Int, title: String?) {
        val array = context.obtainStyledAttributes(resId, R.styleable.titleTextAppearance)
        val titleColor = array.getColor(
            R.styleable.titleTextAppearance_titleColor,
            resources.getColor(R.color.color_565)
        )
        val titleSize =
            array.getDimensionPixelSize(
                R.styleable.titleTextAppearance_titleSize,
                applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f)
            )
        val minWidth = array.getDimensionPixelOffset(R.styleable.titleTextAppearance_minWidth, 0)
        val leftPadding =
            array.getDimensionPixelOffset(R.styleable.titleTextAppearance_leftPadding, 0)
        titleView = TextView(context)
        titleView.setTextColor(titleColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())

        titleView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT
        )

        titleView.minWidth = minWidth
        titleView.setPadding(leftPadding, 0, 0, 0)
        titleView.gravity = Gravity.LEFT or (Gravity.CENTER)
        titleView.text = title
        addView(titleView)
        array.recycle()
    }

    private var topPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (topLine.enable) {
            canvas!!.drawLine(
                topLine.leftMargin.toFloat(), 0f,
                (measuredWidth - topLine.rightMargin).toFloat(), 0f, topPaint
            )

        }
        if (bottomLine.enable) {
            canvas!!.drawLine(
                bottomLine.leftMargin.toFloat(),
                (height - bottomLine.height).toFloat(),
                (measuredWidth - bottomLine.rightMargin).toFloat(),
                (height - bottomLine.height).toFloat(),
                bottomPaint
            )
        }
    }

    /***
     * 将Android使用的单位  sp  dp 转化为px
     */
    private fun applyUnit(unit: Int, value: Float): Int {
        return TypedValue.applyDimension(unit, value, resources.displayMetrics).toInt()
    }

    fun getTitleView(): TextView {
        return titleView
    }

    fun getEditText(): EditText {
        return editText
    }

}