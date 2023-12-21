package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rocks.ui.databinding.ActivityCropBinding
import com.rocks.ui.ratio.CropRatioRecyclerView
import com.rocks.ui.simplecropview.BitmapHolder
import com.rocks.ui.simplecropview.callback.CropCallback
import com.rocks.ui.simplecropview.callback.LoadCallback
import kotlinx.coroutines.launch

class CropActivity : AiBaseActivity<ActivityCropBinding>() {

    private val sourceUri  by lazy { intent?.data }

    private lateinit var _originalBitmap:Bitmap

    companion object{


        fun goToAiCropActivity(activity: Activity, uri: Uri?, launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity, CropActivity::class.java).apply {

                data = uri

            })

        }

    }

    override val mBinding  by lazy { ActivityCropBinding.inflate(layoutInflater) }

    override fun onReadyActivity(savedInstanceState: Bundle?) {

        mBinding.cropRatioRecyclerView.iChangeRatioListener = object :CropRatioRecyclerView.IChangeRatioListener{

            override fun onChangeRatio(width: Int, height: Int) {

                mBinding.cropImageView.setCustomRatio(width,height)

            }

        }

        mBinding.next.setOnClickListener {

            if (::_originalBitmap.isInitialized){

                mBinding.cropImageView.crop(null).execute(object :CropCallback{

                    override fun onError(e: Throwable?) {

                    }

                    override fun onSuccess(cropped: Bitmap?) {

                        setResult(CROP_RQ)

                        BitmapHolder.setBitmap(cropped)

                        finish()

                    }
                })
            }


        }

        mBinding.backBtn.setOnClickListener {

            onBackPressed()

        }


        Glide.with(this).asBitmap().load(sourceUri.toString()).into(object : CustomTarget<Bitmap>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                lifecycleScope.launch {

                    _originalBitmap = resource

                    mBinding.cropImageView.load(resource).useThumbnail(true).execute(object : LoadCallback {

                            override fun onSuccess() {


                            }

                            override fun onError(e: Throwable?) {

                            }

                        })
                }

            }

            override fun onLoadCleared(placeholder: Drawable?) {


            }

        })


    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {

    }


}