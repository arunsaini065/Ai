package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
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
import kotlinx.coroutines.launch

class HomeActivity : AiBaseActivity<ActivityHomeBinding>(),OnBodyHandlerListener,OnCancelFragment {

    override fun onCancel() {

        lifecycleScope.launch {

            _aiUiViewModel.ratioUpdate.collect{

                mBinding.aspectRatioRv.notifySelectedItem(it,lifecycleScope)

            }

        }

        lifecycleScope.launch {

            _aiUiViewModel.styleUpdate.collect{

                mBinding.mStyle.text = it?.modelId

            }
        }

    }


    private val bodyDataHandler by lazy { BodyDataHandler() }

    private val _aiUiViewModel by viewModels<AiUiViewModel>()


    companion object{

        fun goToAiHomeActivity(activity: Activity){

            activity.startActivity(Intent(activity,HomeActivity::class.java))

        }

        fun goToAiHomeActivity(activity: Activity,launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity,HomeActivity::class.java))

        }

    }

    override val mBinding: ActivityHomeBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val _viewModel by viewModels<AiViewModel> { AiViewModelFactory(ModelUseCase(ModelDataRepositoryImpl(Api.createApi()))) }




    override fun onReadyActivity(savedInstanceState: Bundle?) = with(mBinding) {

        _viewModel.postModelIdsList(Api.getBodyOnlyKey(bodyDataHandler))

        mBinding.mStyle.text = bodyDataHandler.modelId


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

                         Toast.makeText(this@HomeActivity,""+it.message,Toast.LENGTH_SHORT).show()

                         progressCircular.beGone()


                     }else if (it is ModelUiState.Loading){

                         progressCircular.beVisible()
                     }

                 }

        }



        aspectRatioRv.iChangeRatioListener= object :CropRatioRecyclerView.IChangeRatioListener{

            override fun onChangeRatio(width: Int, height: Int) {

                bodyDataHandler.aspectRatio = AspectRatio(widthR = width, heightR = height)

                _aiUiViewModel.ratioUpdate.value = bodyDataHandler

            }

        }




        btnGenerate.setOnClickListener {

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

    override fun getHandlerBody():BodyDataHandler{

        return bodyDataHandler
    }


}