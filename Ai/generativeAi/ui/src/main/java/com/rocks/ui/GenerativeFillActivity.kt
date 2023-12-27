package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rocks.BodyDataHandler
import com.rocks.OnBodyHandlerListener
import com.rocks.api.Api
import com.rocks.factory.AiViewModelFactory
import com.rocks.impl.ModelDataRepositoryImpl
import com.rocks.ui.cutout.CheckerboardDrawable
import com.rocks.ui.cutout.CutOut
import com.rocks.ui.cutout.DrawView
import com.rocks.ui.databinding.ActivityGenerativeFillBinding
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel

class GenerativeFillActivity : AiBaseActivity<ActivityGenerativeFillBinding>(),OnBodyHandlerListener {

    private val sourceUri  by lazy { intent?.data }

    private val builder by lazy { CutOut(mBinding.drawView).active()?.init() }
    private val _aiUiViewModel by viewModels<AiUiViewModel>()


    companion object{


        fun goToGenerativeFillActivity(activity: Activity, uri: Uri?, launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity, GenerativeFillActivity::class.java).apply {

                data = uri

            })

        }

    }

    override val mBinding: ActivityGenerativeFillBinding by lazy { ActivityGenerativeFillBinding.inflate(layoutInflater) }

    override fun onReadyActivity(savedInstanceState: Bundle?) {

        mBinding.checkBox.background  = CheckerboardDrawable.create()

        mBinding.backBtn.setOnClickListener {

            finish()
        }

        mBinding.brush.setOnClickListener {

            builder?.setAction(DrawView.DrawViewAction.REPAIR)


            mBinding.brush.setBackgroundResource(R.drawable.generate_output_bg)

            mBinding.eraser.setBackgroundResource(0)

        }

        mBinding.advance.setOnClickListener {

            SettingBtmSheet().show(supportFragmentManager,"ADVANCE")

        }

        mBinding.eraser.setOnClickListener {

            builder?.setAction(DrawView.DrawViewAction.MANUAL_CLEAR)


            mBinding.eraser.setBackgroundResource(R.drawable.generate_output_bg)

            mBinding.brush.setBackgroundResource(0)

        }

        mBinding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {

                    builder?.setBallSize(progress)


            }

            override fun onStartTrackingTouch(seek: SeekBar) {


            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        mBinding.seekbar.progress = 50

        Glide.with(this).asBitmap().load(sourceUri.toString()).into(object : CustomTarget<Bitmap>(){

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                builder?.src(resource)?.bordered()

            }

            override fun onLoadCleared(placeholder: Drawable?) {



            }

        })


    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {


    }

    override fun getHandlerBody(): BodyDataHandler {

        return _aiUiViewModel.bodyDataHandler

    }

}