package com.example.Lab2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
    private val bitmapCanvas = Canvas(bitmap)
    private val paint = Paint()


    // create a empty bitmap on a canvas
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, null, Rect(0, 0, width, height), paint)
    }

    // draw something on the bitmap canvas (not original canvas)
    @RequiresApi(Build.VERSION_CODES.O)
    public fun drawCircle(color: Color){
        paint.color = Color.WHITE
        bitmapCanvas.drawRect(0f,0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        paint.color = color.toArgb()
        bitmapCanvas.drawCircle(0.5f*bitmap.width, 0.5f*bitmap.height,
            0.25f*bitmap.width, paint)
        invalidate();

    }

}