package com.sylvie.boardgameguide.data

import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.annotation.DrawableRes
import com.sylvie.boardgameguide.R

data class Spark(
    val view: View,
    @DrawableRes val animList: Int,
    val duration: Int = 4000
) {

    private var _backgroundAnimationDrawable: AnimationDrawable? = null

    /**
     * Start the gradient animation any time you want.
     */
    fun startAnimation() {
        view.setBackgroundResource(animList)
        _backgroundAnimationDrawable = view.background as? AnimationDrawable
            ?: throw IllegalStateException("View's background is not AnimationDrawable")

        _backgroundAnimationDrawable?.setEnterFadeDuration(duration)
        _backgroundAnimationDrawable?.setExitFadeDuration(duration)

        view.background = _backgroundAnimationDrawable
        view.post {
            _backgroundAnimationDrawable?.start()
        }
    }

    /**
     * Stop the gradient animation any time you want.
     */
    fun stopAnimation() {
        if (_backgroundAnimationDrawable?.isRunning == true) {
            _backgroundAnimationDrawable?.stop()
        }
    }

    companion object {

        @JvmField
        val ANIM_YELLOW_BLUE = R.drawable.anim_spark


    }

}