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
import androidx.activity.viewModels
import androidx.fragment.app.commit
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


    }



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

        textToImageFragment()

        txtToTxt.setOnClickListener {

            textToImageFragment()

        }

        gtFill.setOnClickListener {

            imageToImageFragment(true)

        }


        imgToImg.setOnClickListener {

            imageToImageFragment(false)

        }

        return@with


    }


    private fun textToImageFragment(){

        runCatching {

            supportFragmentManager.commit(true) {

                replace(mBinding.fragmentContainerView.id, TextToImageFragment.getInstance())

            }

            mBinding.txtToTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tic_ext_to_image, 0, 0)

            mBinding.imgToImg.setTextViewDrawableColor(R.color.white)

            mBinding.gtFill.setTextViewDrawableColor(R.color.white)


        }
    }

    private fun imageToImageFragment(fromFill:Boolean){

        runCatching {

            supportFragmentManager.commit(true) {

                replace(mBinding.fragmentContainerView.id, ImageToImageFragment.getInstance(ImageToImageFragment.Args(fromFill)))

            }
            if (fromFill){


                mBinding.txtToTxt.setTextViewDrawableColor(R.color.white)

                mBinding.imgToImg.setTextViewDrawableColor(R.color.white)

                mBinding.gtFill.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.gt_fill, 0, 0)

            }else {

                mBinding.txtToTxt.setTextViewDrawableColor(R.color.white)
                mBinding.imgToImg.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_image_to_txt, 0, 0)
                mBinding.gtFill.setTextViewDrawableColor(R.color.white)

            }

        }
    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {


    }

    override fun getHandlerBody():BodyDataHandler{

        return _aiUiViewModel.bodyDataHandler

    }



}