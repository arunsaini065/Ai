package com.rocks.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.rocks.ui.databinding.ActivityCropBinding

class CropActivity : AiBaseActivity<ActivityCropBinding>() {

    override val mBinding  by lazy { ActivityCropBinding.inflate(layoutInflater) }

    override fun onReadyActivity(savedInstanceState: Bundle?) {

    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {

    }


}