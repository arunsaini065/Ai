package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rocks.BodyDataHandler
import com.rocks.OnBodyHandlerListener
import com.rocks.OnGeneratorListener
import com.rocks.OutPutSingleton
import com.rocks.api.Api
import com.rocks.downloader.FileDownloader
import com.rocks.factory.AiViewModelFactory
import com.rocks.impl.ModelDataRepositoryImpl
import com.rocks.ui.databinding.ActivityResultBinding
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultActivity : AiBaseActivity<ActivityResultBinding>(),OnBodyHandlerListener,OnGeneratorListener {

    override fun onGenerator() {

        runCatching {


            _viewModel.postModelIdBase(Api.getBodyForModel(bodyDataHandler))


        }
    }



    private lateinit var bodyDataHandler:BodyDataHandler

    private lateinit var downloadBitmap:Bitmap

    private val _viewModel by lazy { ViewModelProvider(this, AiViewModelFactory(ModelUseCase(ModelDataRepositoryImpl(Api.createApi()))))[AiViewModel::class.java] }


    companion object{

        fun goToAiResultActivity(activity: Activity){

            activity.startActivity(Intent(activity,ResultActivity::class.java))

        }

        fun goToAiResultActivity(activity: Activity, launcher: ActivityResultLauncher<Intent>){

            launcher.launch(Intent(activity,ResultActivity::class.java))

        }

    }


    override val mBinding: ActivityResultBinding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onReadyActivity(savedInstanceState: Bundle?) {

        if (OutPutSingleton.hasOutput()) {

            val outPutSingleton = OutPutSingleton.getOutPut()

            if (outPutSingleton?.output?.isEmpty() == false){

                mBinding.resultProgressLoader.beVisible()

                Glide.with(this).asBitmap()

                    .load(outPutSingleton.output[0])

                    .addListener(object :RequestListener<Bitmap>{

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {

                            mBinding.resultProgressLoader.beGone()

                            return false

                        }

                        override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {

                            mBinding.resultProgressLoader.beGone()

                            downloadBitmap=resource

                            return false

                        }

                    }).into(mBinding.resultLoader)


            }



        }

        if (OutPutSingleton.hasBodyHandler()){

            val body = OutPutSingleton.getBodyHandler()

            if (body != null) {

                bodyDataHandler = body

            }

            mBinding.positivePrompt.setText( body?.positivePrompt)


        }

        lifecycleScope.launch{

            _viewModel.stateflowAiModel.collect{


                if (it is ModelUiState.Success) {


                    if (it.data != null) {

                        Glide.with(this@ResultActivity).asBitmap()

                            .load(it.data?.output?.get(0))

                            .addListener(object :RequestListener<Bitmap>{

                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {

                                    mBinding.resultProgressLoader.beGone()

                                    return false

                                }

                                override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {

                                    mBinding.resultProgressLoader.beGone()

                                    downloadBitmap=resource

                                    return false

                                }

                            }).into(mBinding.resultLoader)
                    }

                } else if (it is ModelUiState.Error){

                    mBinding.resultProgressLoader.beGone()


                }else if (it is ModelUiState.Loading){

                    mBinding.resultProgressLoader.beVisible()
                }

            }


        }

        mBinding.mDownload.setOnClickListener {

            if (::downloadBitmap.isInitialized){

                FileDownloader(lifecycleScope).saveImage(downloadBitmap){

                    Toast.makeText(this,it.absolutePath,Toast.LENGTH_SHORT).show()

                }

            }

        }

        mBinding.btnGenerate.setOnClickListener {

           onGenerator()

        }


        mBinding.mBackPress.setOnClickListener {

            finish()

        }

        mBinding.positivePrompt.setOnClickListener {

             EditInputBtmSheet().apply {

                 show(supportFragmentManager,"EDIT_INPUT")

             }
        }
    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {




    }

    override fun getHandlerBody() = bodyDataHandler

}