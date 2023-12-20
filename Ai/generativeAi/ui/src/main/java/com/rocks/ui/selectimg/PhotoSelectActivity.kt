package com.rocks.ui.selectimg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rocks.fetcher.MediaViewModel
import com.rocks.ui.AiBaseActivity
import com.rocks.ui.databinding.ActivityPhotoSelectBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PhotoSelectActivity : AiBaseActivity<ActivityPhotoSelectBinding>() {

    private  val _viewModel  by lazy { ViewModelProvider(this)[MediaViewModel::class.java] }

    private val _selectImageAdapter by lazy {

        SelectImageAdapter {

        }

    }

    companion object{

        fun goToAiHomeActivity(activity: Activity){

            activity.startActivity(Intent(activity, PhotoSelectActivity::class.java))

        }

        fun goToAiHomeActivity(activity: Activity, launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity, PhotoSelectActivity::class.java))

        }

    }


    override val mBinding: ActivityPhotoSelectBinding by lazy  { ActivityPhotoSelectBinding.inflate(layoutInflater) }



    override fun onReadyActivity(savedInstanceState: Bundle?) {

        _viewModel.fetchAllData()

        mBinding.backBtn.setOnClickListener {

            onBackPressed()

        }

        mBinding.mRvPhotos.adapter = _selectImageAdapter

        lifecycleScope.launch {

            _viewModel.listOfFolder.collect{


            }
        }

        lifecycleScope.launch {

            _viewModel.listOfImage.collect{

                _selectImageAdapter.submitList(it)

            }
        }

    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {


    }

}