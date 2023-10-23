package com.rocks

import android.util.Size

class AspectRatio (private val height: Int = 512, private val widthR: Int=1, private val heightR: Int=1){

    private fun getRatio(): Float {

        return widthR.toFloat() / heightR.toFloat()

    }

    private fun getWidth(): Int {
        return (height / getRatio()).toInt()
    }

    fun getSize() = Size(getWidth(),height)

    fun toId() = "$widthR/$heightR"
}