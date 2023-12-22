package com.rocks.ui

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.rocks.InspirationData


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

fun isOreoPlus() = Build.VERSION.SDK_INT > Build.VERSION_CODES.O


const val PHOTO_SELECT_RQ = 34
const val CROP_RQ = 35

fun getDummyIns(): MutableList<InspirationData> {

    val list = mutableListOf<InspirationData>()

    repeat(10){

        list.add(InspirationData.DUMMY)

    }

    return list

}


 fun TextView.setTextViewDrawableColor(color: Int) {
    for (drawable in compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
        }
    }
}