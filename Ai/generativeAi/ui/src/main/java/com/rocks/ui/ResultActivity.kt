package com.rocks.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
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
import com.rocks.model.UploadImage
import com.rocks.ui.databinding.ActivityResultBinding
import com.rocks.uistate.ModelUiState
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ResultActivity : AiBaseActivity<ActivityResultBinding>(),OnBodyHandlerListener,OnGeneratorListener,OnCancelFragment {

    override fun onGenerator() {

        runCatching {

             if (::bodyDataHandler.isInitialized){

                _viewModel.postModelIdBase(bodyDataHandler)

             }


        }
    }

    override fun onCancel() {

        if (::bodyDataHandler.isInitialized) {

            mBinding.positivePrompt.setText(bodyDataHandler.positivePrompt)

            mBinding.positivePrompt.setText(bodyDataHandler.positivePrompt)

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


        if (OutPutSingleton.hasBodyHandler()){

            val body = OutPutSingleton.getBodyHandler()

            if (body != null) {

                bodyDataHandler = body

            }

            mBinding.positivePrompt.setText(body?.positivePrompt)

            if (body != null) {

                 if (body.isAddImage){

                     _viewModel.uploadImage(Api.getBodyForUploadImage(bodyDataHandler))

                 }else {

                     _viewModel.postModelIdBase(body)

                 }


            }


        }

        lifecycleScope.launch {


         _viewModel.stateflowUploadImage.collect{

             if (it is ModelUiState.Success){

                 mBinding.resultLoaderProgress.beGone()

                 if (it.data!=null) {

                     bodyDataHandler.uploadImage = it.data


                     _viewModel.postModelIdBase(bodyDataHandler)
                 }
             }else if (it is ModelUiState.Loading){

                 mBinding.resultLoaderProgress.beVisible()

             } else if (it is ModelUiState.Processing){

                 Toast.makeText(this@ResultActivity,"Processing",Toast.LENGTH_SHORT).show()

             } else if (it is ModelUiState.Error){
                 mBinding.resultLoaderProgress.beGone()

                 Toast.makeText(this@ResultActivity,""+it.message,Toast.LENGTH_SHORT).show()

             }


         }


        }

        lifecycleScope.launch{

            _viewModel.stateflowAiModel.collect{


                if (it is ModelUiState.Success) {


                    if (it.data != null) {

                        Glide.with(this@ResultActivity).asBitmap()

                            .load(it.data?.output?.get(0))

                            .addListener(object :RequestListener<Bitmap>{

                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {

                                    mBinding.resultLoaderProgress.beGone()

                                    return false

                                }

                                override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {

                                    mBinding.resultLoaderProgress.beGone()

                                    downloadBitmap=resource

                                    return false

                                }

                            }).into(mBinding.resultLoader)
                    }

                } else if (it is ModelUiState.Error){

                    mBinding.resultLoaderProgress.beGone()

                    Toast.makeText(this@ResultActivity,""+it.message,Toast.LENGTH_SHORT).show()


                }else if (it is ModelUiState.Loading){

                    mBinding.resultLoaderProgress.beVisible()

                } else if (it is ModelUiState.Processing){

                    Toast.makeText(this@ResultActivity,"Processing",Toast.LENGTH_SHORT).show()

                }

            }


        }

        mBinding.save.setOnClickListener {

            if (::downloadBitmap.isInitialized){

                FileDownloader(lifecycleScope).saveImage(downloadBitmap){

                    MediaScannerConnection(this,object :MediaScannerConnection.MediaScannerConnectionClient{
                        override fun onScanCompleted(path: String?, uri: Uri?) {

                        }

                        override fun onMediaScannerConnected() {

                        }
                    }).scanFile(it.absolutePath,"image/png")
                }

            }

        }



        mBinding.mBackPress.setOnClickListener {

            finish()

        }

        mBinding.btnMoreVariate.setOnClickListener {

            showMoreVariateSheet {


            }

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