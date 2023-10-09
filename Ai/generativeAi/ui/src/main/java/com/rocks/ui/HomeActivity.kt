package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rocks.AspectRatio
import com.rocks.BodyDataHandler
import com.rocks.OnBodyHandlerListener
import com.rocks.OutPutSingleton
import com.rocks.api.Api
import com.rocks.factory.AiViewModelFactory
import com.rocks.impl.ModelDataRepositoryImpl
import com.rocks.ui.databinding.ActivityHomeBinding
import com.rocks.ui.ratio.CropRatioRecyclerView
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeActivity : AiBaseActivity<ActivityHomeBinding>(),OnBodyHandlerListener {


    private val bodyDataHandler by lazy { BodyDataHandler() }


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


           ModelBtmSheet().apply {

                show(supportFragmentManager,"MODEL_BTM")

            }

        }

        mBinding.advanceChoose.setOnClickListener {

        SettingBtmSheet().apply {

                show(supportFragmentManager,"SETTING_BTM")

            }
        }
        lifecycleScope.launch {


                 _viewModel.stateflowAiModel.collect{

                     if (it is ModelUiState.Success) {

                         progressCircular.beGone()

                         if (it.data != null) {

                             OutPutSingleton.setOutput(it.data)

                             OutPutSingleton.setBodyHandler(bodyDataHandler)

                             ResultActivity.goToAiResultActivity(this@HomeActivity, activityLauncher)
                         }

                     } else if (it is ModelUiState.Error){

                         progressCircular.beGone()


                     }else if (it is ModelUiState.Loading){

                         progressCircular.beVisible()
                     }

                 }

        }

        aspectRatioRv.iChangeRatioListener= object :CropRatioRecyclerView.IChangeRatioListener{

            override fun onChangeRatio(width: Int, height: Int) {

                bodyDataHandler.aspectRatio = AspectRatio(widthR = width, heightR = height)

            }

        }

        btnGenerate.setOnClickListener {

            OutPutSingleton.setBodyHandler(bodyDataHandler)
            ResultActivity.goToAiResultActivity(this@HomeActivity, activityLauncher)

            runCatching {

                val prompt = positivePrompt.text?.toString()?:""

                if (prompt.isEmpty().not()) {

                    bodyDataHandler.positivePrompt=prompt

                    _viewModel.postModelIdBase(Api.getBodyForModel(bodyDataHandler))

                }

            }



        }


    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {


    }

    override fun getHandlerBody() = bodyDataHandler


}