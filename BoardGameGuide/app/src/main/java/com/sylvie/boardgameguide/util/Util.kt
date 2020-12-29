package com.sylvie.boardgameguide.util

import android.app.Activity
import android.view.View
import android.view.animation.AccelerateInterpolator
import me.samlss.bloom.Bloom
import me.samlss.bloom.effector.BloomEffector
import me.samlss.bloom.shape.distributor.CircleShapeDistributor
import me.samlss.bloom.shape.distributor.ParticleShapeDistributor

object Util {

    var mBloom: Bloom? = null

    fun boom(view: View, activity: Activity?) {
        val shapeDistributor: ParticleShapeDistributor = CircleShapeDistributor()

        mBloom?.cancel()
        mBloom = Bloom.with(activity as Activity)
            .setParticleRadius(15F)
            .setShapeDistributor(shapeDistributor)
            .setEffector(
                BloomEffector.Builder()
                    .setDuration(2000)
                    .setScaleRange(0.5f, 1.5f)
                    .setRotationSpeedRange(0.01f, 0.05f)
                    .setSpeedRange(0.1f, 0.5f)
                    .setAcceleration(0.00025f, 90)
                    .setAnchor((view.getWidth() / 2).toFloat(), view.getHeight().toFloat())
                    .setFadeOut(500, AccelerateInterpolator())
                    .build())
        mBloom?.boom(view)
    }
}