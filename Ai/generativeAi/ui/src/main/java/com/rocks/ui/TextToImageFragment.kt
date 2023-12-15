package com.rocks.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.rocks.AspectRatio
import com.rocks.BodyDataHandler
import com.rocks.OutPutSingleton
import com.rocks.api.Api
import com.rocks.factory.AiViewModelFactory
import com.rocks.impl.ModelDataRepositoryImpl
import com.rocks.ui.databinding.TextToImageFragmentBinding
import com.rocks.ui.ratio.CropRatioRecyclerView
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel
import kotlinx.coroutines.launch

class TextToImageFragment : AiBaseFragment<TextToImageFragmentBinding>(),OnCancelFragment {

    companion object{

        fun getInstance() = TextToImageFragment()

    }


    override fun onCancel() {

        lifecycleScope.launch {

            _aiUiViewModel.ratioUpdate.collect{

                mBinding.aspectRatioRv.notifySelectedItem(it,lifecycleScope)

            }

        }

        lifecycleScope.launch {

            _aiUiViewModel.modelUpdate.collect{

                mBinding.mModelTv.text = it?.modelId?:bodyDataHandler.modelId

                mBinding.mStyleTv.text = it?.loraModel?:bodyDataHandler.loraModel

            }
        }

    }


    override val mBinding: TextToImageFragmentBinding by lazy {  TextToImageFragmentBinding.inflate(layoutInflater)}


    private val _viewModel by activityViewModels <AiViewModel> { AiViewModelFactory(ModelUseCase(ModelDataRepositoryImpl(Api.createApi()))) }

    private val bodyDataHandler by lazy { BodyDataHandler() }

    private val _aiUiViewModel by activityViewModels<AiUiViewModel>()


    override fun onReadyCreateView(savedInstanceState: Bundle?) {


    }

    override fun onReadyView(view: View, savedInstanceState: Bundle?) = with(mBinding) {


        _viewModel.postModelIdsList(Api.getBodyOnlyKey(bodyDataHandler))

        mModelTv.text = bodyDataHandler.modelId

        mStyleTv.text = bodyDataHandler.loraModel

        mModelTv.setOnClickListener {


            ModelBtmSheet().apply {

                show(this@TextToImageFragment.childFragmentManager,"MODEL_BTM")

            }

        }

        mStyleTv.setOnClickListener {


            ModelBtmSheet().apply {

                arguments = Bundle().apply {

                    putBoolean("is_style",true)

                }

                show(this@TextToImageFragment.childFragmentManager,"MODEL_BTM")

            }

        }

        advanceChoose.setOnClickListener {

            SettingBtmSheet().apply {

                show(this@TextToImageFragment.childFragmentManager,"SETTING_BTM")

            }
        }
        lifecycleScope.launch {


            _viewModel.stateflowAiModel.collect{

                if (it is ModelUiState.Success) {


                    if (it.data != null) {

                        OutPutSingleton.setOutput(it.data)

                        OutPutSingleton.setBodyHandler(bodyDataHandler)

                        ResultActivity.goToAiResultActivity(requireActivity(), activityLauncher)
                    }

                } else if (it is ModelUiState.Error){

                    Toast.makeText(context,""+it.message, Toast.LENGTH_SHORT).show()



                }else if (it is ModelUiState.Loading){

                }

            }

        }



        aspectRatioRv.iChangeRatioListener= object : CropRatioRecyclerView.IChangeRatioListener{

            override fun onChangeRatio(width: Int, height: Int) {

                bodyDataHandler.aspectRatio = AspectRatio(widthR = width, heightR = height)

                _aiUiViewModel.ratioUpdate.value = bodyDataHandler

            }

        }


        btnGenerate.setOnClickListener {

            runCatching {

                if (bodyDataHandler.isAddImage && bodyDataHandler.uploadImage==null) {

                    _viewModel.uploadImage(Api.getBodyForUploadImage(bodyDataHandler))

                } else {

                    val prompt = positivePrompt.text?.toString() ?: ""

                    if (prompt.isEmpty().not()) {

                        bodyDataHandler.positivePrompt = prompt

                        _viewModel.postModelIdBase(bodyDataHandler)

                    }

                }
            }



        }

        lifecycleScope.launch {

            _viewModel.stateflowUploadImage.collect{

                when (it) {
                    is ModelUiState.Success -> {

                        if (it.data!=null) {

                            bodyDataHandler.uploadImage = it.data


                            _viewModel.postModelIdBase(bodyDataHandler)
                        }

                    }

                    is ModelUiState.Error -> {

                        Toast.makeText(requireContext(),""+it.message, Toast.LENGTH_SHORT).show()



                    }

                    is ModelUiState.Loading -> {

                    }

                    else -> {

                    }
                }


            }

        }

        return@with


    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {



    }


}