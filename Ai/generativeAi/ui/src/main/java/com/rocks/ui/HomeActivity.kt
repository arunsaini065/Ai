package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.activity.result.ActivityResultLauncher

import androidx.lifecycle.ViewModelProvider
import com.rocks.api.Api
import com.rocks.factory.AiViewModelFactory
import com.rocks.impl.ModelDataRepositoryImpl
import com.rocks.ui.databinding.ActivityHomeBinding
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel


class HomeActivity : AiBaseActivity<ActivityHomeBinding>() {


    companion object{

        fun goToAiHomeActivity(activity: Activity){

            activity.startActivity(Intent(activity,HomeActivity::class.java))

        }

        fun goToAiHomeActivity(activity: Activity,launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity,HomeActivity::class.java))

        }

    }

    override val mBinding: ActivityHomeBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val _viewModel by lazy { ViewModelProvider(this,AiViewModelFactory(ModelUseCase(ModelDataRepositoryImpl(Api.createApi()))))[AiViewModel::class.java] }


    override fun onReadyActivity(savedInstanceState: Bundle?) = with(mBinding) {


        mBinding.mStyle.setOnClickListener {

            var btsheet = ModelBtmSheet()
            btsheet.show(supportFragmentManager,"")

        }

        mBinding.advanceChoose.setOnClickListener {

            var btsheet = SettingBtmSheet()
            btsheet.show(supportFragmentManager,"")
        }


    }





}