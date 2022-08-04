package com.ops.opside.common.Utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


@SuppressLint("ClickableViewAccessibility")
fun FloatingActionButton.animateOnPress() {
    this.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val scaleX = ObjectAnimator.ofFloat(
                        view,
                        "scaleX", 0.9f
                    )
                    val scaleY = ObjectAnimator.ofFloat(
                        view,
                        "scaleY", 0.9f
                    )
                    scaleX.duration = 200
                    scaleY.duration = 200
                    val scaleDown = AnimatorSet()
                    scaleDown.play(scaleX).with(scaleY)
                    scaleDown.start()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    val scaleX = ObjectAnimator.ofFloat(
                        view,
                        "scaleX", 1f
                    )
                    val scaleY = ObjectAnimator.ofFloat(
                        view,
                        "scaleY", 1f
                    )
                    scaleX.duration = 200
                    scaleY.duration = 200
                    val scaleUp = AnimatorSet()
                    scaleUp.play(scaleX).with(scaleY)
                    scaleUp.start()

                    performClick()
                    return true
                }
                else -> return false
            }
        }
    })
}

@SuppressLint("ClickableViewAccessibility")
fun ImageButton.animateOnPress() {
    this.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val scaleX = ObjectAnimator.ofFloat(
                        view,
                        "scaleX", 0.9f
                    )
                    val scaleY = ObjectAnimator.ofFloat(
                        view,
                        "scaleY", 0.9f
                    )
                    scaleX.duration = 200
                    scaleY.duration = 200
                    val scaleDown = AnimatorSet()
                    scaleDown.play(scaleX).with(scaleY)
                    scaleDown.start()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    val scaleX = ObjectAnimator.ofFloat(
                        view,
                        "scaleX", 1f
                    )
                    val scaleY = ObjectAnimator.ofFloat(
                        view,
                        "scaleY", 1f
                    )
                    scaleX.duration = 200
                    scaleY.duration = 200
                    val scaleUp = AnimatorSet()
                    scaleUp.play(scaleX).with(scaleY)
                    scaleUp.start()

                    performClick()
                    return true
                }
                else -> return false
            }
        }
    })
}
