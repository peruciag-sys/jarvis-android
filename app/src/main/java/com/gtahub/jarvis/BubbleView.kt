package com.gtahub.jarvis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import kotlin.math.abs

class BubbleView(context: Context) : FrameLayout(context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4CAF50")
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }
    private var isExpanded = false
    private var radius = 50f

    init {
        setWillNotDraw(false)
        setOnTouchListener(BubbleTouchListener())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(60f, 60f, radius, paint)
        canvas.drawText("J", 60f, 75f, textPaint)
    }

    private inner class BubbleTouchListener : OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event == null) return false

            return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = (layoutParams as WindowManager.LayoutParams).x
                    initialY = (layoutParams as WindowManager.LayoutParams).y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - initialTouchX).toInt()
                    val deltaY = (event.rawY - initialTouchY).toInt()

                    val params = layoutParams as WindowManager.LayoutParams
                    params.x = initialX + deltaX
                    params.y = initialY + deltaY

                    try {
                        windowManager.updateViewLayout(this@BubbleView, params)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    val deltaX = abs(event.rawX - initialTouchX)
                    val deltaY = abs(event.rawY - initialTouchY)

                    if (deltaX < 10 && deltaY < 10) {
                        onBubbleClicked()
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun onBubbleClicked() {
        isExpanded = !isExpanded
        radius = if (isExpanded) 70f else 50f
        invalidate()
    }
}
