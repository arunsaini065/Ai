package com.rocks.ui.cutout

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.util.Pair
import java.util.Stack

data class DrawViewState(var stateName: DrawView.DrawViewAction?, var bitmap: Bitmap?, var cuts: Stack<Pair<Pair<Path, Paint>?, Bitmap?>>?, var undoCuts:Stack<Pair<Pair<Path, Paint>?, Bitmap?>>?)