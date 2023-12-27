package com.rocks.ui

import android.app.Activity
import android.content.res.Resources
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
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rocks.InspirationData
import com.rocks.ui.databinding.MoreVariateBottomSheetBinding
import com.rocks.ui.databinding.TryInspirationBinding


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

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts DP to Pixel.
 */
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()



 fun TextView.setTextViewDrawableColor(color: Int) {
    for (drawable in compoundDrawables) {
        if (drawable != null) {
            drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
        }
    }
}

fun Activity.showMoreVariateSheet(fromGenerate: Boolean, callback: () -> Unit){

    val binding = MoreVariateBottomSheetBinding.inflate(layoutInflater)

    BottomSheetDialog(this,R.style.BootomSheetDialogTheme).apply {


        setContentView(binding.root)

        show()

        with(binding){

            if (fromGenerate){

                btnMoreGenerate.text = getString(R.string.generate)

            }

            btnMoreGenerate.setOnClickListener {

                callback()

                dismiss()

            }

        }

    }



}


fun Activity.showTryInspiration(inspirationData: InspirationData,callback:(InspirationData)->Unit){

    val binding = TryInspirationBinding.inflate(layoutInflater)



    BottomSheetDialog(this,R.style.BootomSheetDialogTheme).apply {


        setContentView(binding.root)

        show()

        with(binding){

            binding.modelId.text = inspirationData.modelId

            binding.description.text = inspirationData.description

            Glide.with(binding.modelThumbnail).load(inspirationData.imageUrl).centerCrop().into(binding.modelThumbnail)

            btnTry.setOnClickListener {

                callback(inspirationData)

                dismiss()

            }

        }


    }.apply {

        behavior.peekHeight = 800.dpToPx

    }


}
