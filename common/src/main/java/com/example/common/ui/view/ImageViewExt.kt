package com.example.common.ui.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.Image
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun ImageView.loadUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadCircleUrl(url: String) {
    Glide.with(this).load(url).transform(CenterCrop()).into(this)
}

fun ImageView.loadCorner(url: String, corner: Int) {
    Glide.with(this).load(url).transform(CenterCrop(), RoundedCorners(corner)).into(this)
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE
) {
    Glide.with(this).load(url).transform().into(this)

}


class BorderTransform(private val borderWidth: Float, borderColor: Int) : CircleCrop() {
    private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)
        val halfWidth = outWidth / 2.toFloat()
        val halfHeight = outHeight / 2.toFloat()
        canvas.drawCircle(
            halfWidth,
            halfHeight,
            halfHeight.coerceAtMost(halfWidth) - borderWidth / 2,
            borderPaint
        )
        canvas.setBitmap(null)
        return transform
    }

}