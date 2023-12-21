package com.rocks.ui.selectimg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rocks.fetcher.MediaViewModel
import com.rocks.ui.AiBaseActivity
import com.rocks.ui.PHOTO_SELECT_RQ
import com.rocks.ui.databinding.ActivityPhotoSelectBinding
import kotlinx.coroutines.launch

class PhotoSelectActivity : AiBaseActivity<ActivityPhotoSelectBinding>() {

    private  val _viewModel  by lazy { ViewModelProvider(this)[MediaViewModel::class.java] }

    private val _selectImageAdapter by lazy {

        SelectImageAdapter {

            setResult(PHOTO_SELECT_RQ,Intent().apply {

                data = it.uri.toUri()

            })

            finish()

        }

    }

    companion object{

        fun goToAiPhotoActivity(activity: Activity, launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity, PhotoSelectActivity::class.java))

        }

    }


    override val mBinding: ActivityPhotoSelectBinding by lazy  { ActivityPhotoSelectBinding.inflate(layoutInflater) }



    override fun onReadyActivity(savedInstanceState: Bundle?) {

        _viewModel.fetchAllData()

        lifecycleScope.launch {

            _viewModel.folderName.collect{

                mBinding.selectedBuketName.text = it

            }

        }

        mBinding.selectedBuketName.setOnClickListener {

            FolderBottomSheet().show(supportFragmentManager,"FOLDER")

        }

        mBinding.backBtn.setOnClickListener {

            onBackPressed()

        }

        mBinding.mRvPhotos.adapter = _selectImageAdapter

        lifecycleScope.launch {

            _viewModel.listOfImage.collect{

                _selectImageAdapter.submitList(it)

            }
        }

    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {


    }

}