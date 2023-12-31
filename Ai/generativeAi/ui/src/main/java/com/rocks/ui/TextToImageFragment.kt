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
import com.rocks.convertBase64
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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

            val bitmap = BitmapHolder.getBitmap()

            lifecycleScope.launch(Dispatchers.IO) {

                bodyDataHandler.base64 = bitmap?.convertBase64()

            }
            bodyDataHandler.isAddImage = true
            mBinding.uploadedImg.setImageBitmap(bitmap)

        }

        mBinding.changeTxt.setOnClickListener {

            PhotoSelectActivity.goToAiPhotoActivity(requireActivity(),activityLauncher)

        }


        _viewModel.postModelIdsList(Api.getBodyOnlyKey(bodyDataHandler))

        lifecycleScope.launch {

            _viewModel.getAllInspiration(requireContext()).collect{

                mInspirationRv.apply {

                    layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

                    adapter = InspirationAdapter{

                        activity?.showTryInspiration(it){

                            bodyDataHandler.positivePrompt = it.description

                            bodyDataHandler.modelId = it.modelId

                            mModelTv.text = bodyDataHandler.modelId

                            mBinding.positivePrompt.setText(bodyDataHandler.positivePrompt)

                        }

                    }.apply {

                        submitList(it)

                    }

                }

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


        aspectRatioRv.iChangeRatioListener= object : CropRatioRecyclerView.IChangeRatioListener{

            override fun onChangeRatio(width: Int, height: Int) {

                bodyDataHandler.aspectRatio = AspectRatio(widthR = width, heightR = height)

                _aiUiViewModel.ratioUpdate.value = bodyDataHandler

            }

        }


        btnMoreGenerate.setOnClickListener {

            runCatching {

                val prompt = positivePrompt.text?.toString() ?: ""

                if (prompt.isEmpty().not()) {

                    bodyDataHandler.positivePrompt = prompt

                }else{

                    return@runCatching
                }

                OutPutSingleton.setBodyHandler(bodyDataHandler)

                ResultActivity.goToAiResultActivity(requireActivity(), activityLauncher)

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
                val bitmap = BitmapHolder.getBitmap()
                 lifecycleScope.launch(Dispatchers.IO) {

                     bodyDataHandler.base64 = bitmap?.convertBase64()

                 }
                bodyDataHandler.isAddImage = true
                mBinding.uploadedImg.setImageBitmap(bitmap)

            }

        }

    }


}