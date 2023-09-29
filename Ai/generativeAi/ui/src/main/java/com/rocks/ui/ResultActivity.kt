package com.rocks.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.bumptech.glide.Glide
import com.rocks.ui.databinding.ActivityResultBinding

class ResultActivity : AiBaseActivity<ActivityResultBinding>() {



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

        Glide.with(this).load("https://cdn.pixabay.com/photo/2018/04/26/16/39/beach-3352363_1280.jpg").into(mBinding.resultLoader)

        mBinding.mBackPress.setOnClickListener {

            finish()

        }
    }

}