package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rocks.AspectRatio
import com.rocks.BodyDataHandler
import com.rocks.OnBodyHandlerListener
import com.rocks.OutPutSingleton
import com.rocks.api.Api
import com.rocks.factory.AiViewModelFactory
import com.rocks.impl.ModelDataRepositoryImpl
import com.rocks.ui.databinding.ActivityHomeBinding
import com.rocks.ui.imageloader.ImageLoader
import com.rocks.ui.ratio.CropRatioRecyclerView
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


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

        mStyle.text = bodyDataHandler.modelId


        uploadImageLayout.setOnClickListener {


            imageLauncher.launch("image/*")


        }

        mStyle.setOnClickListener {


           ModelBtmSheet().apply {

                show(supportFragmentManager,"MODEL_BTM")

            }

        }


        advanceChoose.setOnClickListener {

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

        closeImg.setOnClickListener {

            bodyDataHandler.uploadImage=null

            bodyDataHandler.isAddImage=false

            mBinding.mGroupSelectImage.beGone()

            mBinding.uploadTxt.beVisible()

            mBinding.addIcon.setImageResource(R.drawable.baseline_add_24)


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

                            progressCircular.beGone()

                            _viewModel.postModelIdBase(bodyDataHandler)
                        }

                    }

                    is ModelUiState.Error -> {

                        Toast.makeText(this@HomeActivity,""+it.message,Toast.LENGTH_SHORT).show()

                        progressCircular.beGone()


                    }

                    is ModelUiState.Loading -> {

                        progressCircular.beVisible()
                    }
                }


            }

        }

        return@with


    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {


    }

    override fun getHandlerBody():BodyDataHandler{

        return bodyDataHandler
    }

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { it ->

        if (it==null){

            return@registerForActivityResult
        }

        mBinding.mGroupSelectImage.beVisible()

        mBinding.uploadTxt.beGone()

        mBinding.progressCircular.beVisible()


        mBinding.addIcon.setImageResource(R.drawable.baseline_keyboard_arrow_right_24)

        Glide.with(this).asBitmap().load(it).transform( CenterCrop(), RoundedCorners(10)).into(mBinding.mUploadThumbnail)

        ImageLoader(it,window.decorView){ bitmap ->

            lifecycleScope.launch(Dispatchers.IO) {

                runCatching {


                    ByteArrayOutputStream().use {

                        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)

                        bodyDataHandler.base64 =  Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)

                        bodyDataHandler.isAddImage = true

                        bodyDataHandler.uploadImage = null

                        withContext(Dispatchers.Main){

                            mBinding.progressCircular.beGone()

                        }


                    }



                }





            }

        }.execute()



    }


}