package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rocks.OutPutSingleton
import com.rocks.downloader.FileDownloader
import com.rocks.ui.databinding.ActivityResultBinding

class ResultActivity : AiBaseActivity<ActivityResultBinding>() {


    private lateinit var downloadBitmap:Bitmap

    companion object{

        fun goToAiResultActivity(activity: Activity){

            activity.startActivity(Intent(activity,ResultActivity::class.java))

        }

        fun goToAiResultActivity(activity: Activity, launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity,ResultActivity::class.java))

        }

    }


    override val mBinding: ActivityResultBinding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onReadyActivity(savedInstanceState: Bundle?) {

        if (OutPutSingleton.hasOutput()) {

            val outPutSingleton = OutPutSingleton.getOutPut()

//            if (outPutSingleton?.output?.isEmpty() == false){
//
//                mBinding.resultProgressLoader.beVisible()
//
//                Glide.with(this).asBitmap()
//
//                    .load(outPutSingleton.output[0])
//
//                    .addListener(object :RequestListener<Bitmap>{
//
//                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
//
//                            mBinding.resultProgressLoader.beGone()
//
//                            return false
//
//                        }
//
//                        override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
//
//                            mBinding.resultProgressLoader.beGone()
//
//                            downloadBitmap=resource
//
//                            return false
//
//                        }
//
//                    }).into(mBinding.resultLoader)
//
//
//            }



        }

        mBinding.mDownload.setOnClickListener {

            if (::downloadBitmap.isInitialized){

                FileDownloader(lifecycleScope).saveImage(downloadBitmap){

                    Toast.makeText(this,it.absolutePath,Toast.LENGTH_SHORT).show()

                }

            }

        }


        mBinding.mBackPress.setOnClickListener {

            finish()

        }

        mBinding.editInput.setOnClickListener {
            var sheet = EditInputBtmSheet()
            sheet.show(supportFragmentManager,"")
        }
    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {




    }

}