package com.rocks.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.bumptech.glide.Glide
import com.rocks.OutPutSingleton
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

        if (OutPutSingleton.hasOutput()) {

            val outPutSingleton = OutPutSingleton.getOutPut()

            if (outPutSingleton?.output?.isEmpty() == false){

                Glide.with(this)
                    .load(outPutSingleton.output[0])
                    .into(mBinding.resultLoader)


            }



        }
        mBinding.mBackPress.setOnClickListener {

            finish()

        }
    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {




    }

}