package util

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat


/**
 * @author yarolegovich https://github.com/yarolegovich
 * 04.02.2017.
 */
class ResUtil(private val context: Context) {
    @ColorInt
    fun color(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    fun dimen(@DimenRes dimRes: Int): Int {
        return Math.round(context.resources.getDimension(dimRes))
    }

}
