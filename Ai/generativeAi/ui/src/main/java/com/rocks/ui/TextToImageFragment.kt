package com.rocks.ui

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rocks.AspectRatio
import com.rocks.BodyDataHandler
import com.rocks.OutPutSingleton
import com.rocks.api.Api
import com.rocks.factory.AiViewModelFactory
import com.rocks.impl.ModelDataRepositoryImpl
import com.rocks.ui.databinding.TextToImageFragmentBinding
import com.rocks.ui.inspiration.InspirationAdapter
import com.rocks.ui.ratio.CropRatioRecyclerView
import com.rocks.ui.selectimg.PhotoSelectActivity
import com.rocks.ui.simplecropview.BitmapHolder
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class TextToImageFragment : AiBaseFragment<TextToImageFragmentBinding>(),OnCancelFragment {

    private val args by lazy {  arguments?.getParcelable("args")?: Args() }




    @Parcelize
    data class Args(val fromImgToImg:Boolean = false,val uri: Uri?=null): Parcelable

    companion object{

        fun getInstance(args: Args = Args()) = TextToImageFragment().apply {

            arguments = Bundle().apply {

                putParcelable("args", args)

            }

        }

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



        if (args.fromImgToImg){

            mBinding.uploadedImgGp.beVisible()

            mBinding.mInspirationRv.beGone()

            mBinding.inspirationTxt.beGone()

            mBinding.uploadedImg.setImageBitmap(BitmapHolder.getBitmap())

        }

        mBinding.changeTxt.setOnClickListener {

            PhotoSelectActivity.goToAiPhotoActivity(requireActivity(),activityLauncher)

        }


        _viewModel.postModelIdsList(Api.getBodyOnlyKey(bodyDataHandler))

        mInspirationRv.apply {

            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

            adapter = InspirationAdapter().apply {

                submitList(getDummyIns())

            }

        }

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



                }
            }

        }



        aspectRatioRv.iChangeRatioListener= object : CropRatioRecyclerView.IChangeRatioListener{

            override fun onChangeRatio(width: Int, height: Int) {

                bodyDataHandler.aspectRatio = AspectRatio(widthR = width, heightR = height)

                _aiUiViewModel.ratioUpdate.value = bodyDataHandler

            }

        }


        btnMoreGenerate.setOnClickListener {
            ResultActivity.goToAiResultActivity(requireActivity())


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

        if (activityResult.resultCode== PHOTO_SELECT_RQ){

             runCatching {

                 CropActivity.goToAiCropActivity(requireActivity(), activityResult.data?.data, activityLauncher)

             }

        }else if (activityResult.resultCode == CROP_RQ){
            runCatching {

                mBinding.uploadedImg.setImageBitmap(BitmapHolder.getBitmap())

            }

        }

    }


}