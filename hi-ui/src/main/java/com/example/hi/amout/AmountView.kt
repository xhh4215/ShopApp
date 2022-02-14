package com.example.hi.amout
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding

class AmountView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var amountValueChangedCallback: (Int) -> Unit
    private val amountViewAttrs = AttrsParse.parseAmountViewAttrs(context, attrs, defStyleAttr)
    private var amountValue = amountViewAttrs.amountValue


    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        applyAttrs()
    }

    private fun applyAttrs() {
        val increaseButton = generateBtn()
        increaseButton.text = "+"
        val decreaseButton = generateBtn()
        increaseButton.text = "-"
        val amountView = generateAmountTextView()
        amountView.text = amountValue.toString()
        addView(decreaseButton)
        addView(amountView)
        addView(increaseButton)
        decreaseButton.isEnabled = amountValue > amountViewAttrs.amountMinValue
        increaseButton.isEnabled = amountValue < amountViewAttrs.amountMaxValue


        decreaseButton.setOnClickListener {
            amountValue--
            amountView.text = amountValue.toString()
            decreaseButton.isEnabled = amountValue > amountViewAttrs.amountMinValue
            increaseButton.isEnabled = true
            amountValueChangedCallback(amountValue)
        }
        increaseButton.setOnClickListener {
            amountValue++
            amountView.text = amountValue.toString()
            increaseButton.isEnabled = amountValue < amountViewAttrs.amountMaxValue
            decreaseButton.isEnabled = true
            amountValueChangedCallback(amountValue)
        }

    }

    private fun generateBtn(): Button {
        val button = Button(context)
        button.setBackgroundResource(0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.elevation = 0f
        }
        button.setPadding(0)
        button.includeFontPadding = false
        button.setTextColor(amountViewAttrs.btnTextColor)
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, amountViewAttrs.btnTextSize)
        button.setBackgroundColor(amountViewAttrs.btnBackground)
        button.gravity = Gravity.CENTER
        button.layoutParams = LayoutParams(amountViewAttrs.btnSize, amountViewAttrs.btnSize)
        return button
    }

    private fun generateAmountTextView(): TextView {
        val view = TextView(context)
        view.setPadding(0)
        view.setTextColor(amountViewAttrs.amountTextColor)
        view.setBackgroundColor(amountViewAttrs.amountBackground)
        view.gravity = Gravity.CENTER
        view.includeFontPadding = false
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.leftMargin = amountViewAttrs.margin
        params.rightMargin = amountViewAttrs.margin
        view.layoutParams = params
        view.minWidth = amountViewAttrs.amountSize

        return view
    }

    fun getAmountValue(): Int {
        return amountValue
    }

    fun setAmountValueChangedListener(amountValueChangedCallback: (Int) -> Unit) {
        this.amountValueChangedCallback = amountValueChangedCallback
    }
}