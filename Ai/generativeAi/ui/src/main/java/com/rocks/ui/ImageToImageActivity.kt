package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.commit
import com.rocks.ui.databinding.ActivityImageToImageBinding

class ImageToImageActivity : AiBaseActivity<ActivityImageToImageBinding>() {

    private val sourceUri  by lazy { intent?.data }

    companion object{


        fun goToAiImgToImgActivity(activity: Activity, uri: Uri?, launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity, ImageToImageActivity::class.java).apply {

                data = uri

            })

        }

    }

    override val mBinding: ActivityImageToImageBinding by lazy { ActivityImageToImageBinding.inflate(layoutInflater) }

    override fun onReadyActivity(savedInstanceState: Bundle?) {

        mBinding.backBtn.setOnClickListener {

            onBackPressed()
        }

        replaceTextToImageFragment()

    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {


    }


    private fun replaceTextToImageFragment(){


        runCatching {

            supportFragmentManager.commit(true) {

                replace(mBinding.container.id, TextToImageFragment.getInstance(TextToImageFragment.Args(true,sourceUri)))

            }



        }
    }


}