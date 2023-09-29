package com.rocks.ui

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding


inline fun <reified V : ViewDataBinding> ViewGroup.toBinding(): V {
    return V::class.java.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    ).invoke(null, LayoutInflater.from(context), this, false) as V
}

fun View?.beVisible(){
    this?.visibility = View.VISIBLE
}

fun View?.beGone(){
    this?.visibility =View.GONE
}

fun ImageView.setTintColor(color:Int){
    this.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
}