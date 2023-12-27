package com.rocks.ui

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rocks.ui.databinding.ImageToImageFragmentBinding
import com.rocks.ui.discovermore.DiscoverMoreAdapter
import com.rocks.ui.selectimg.PhotoSelectActivity
import com.rocks.ui.simplecropview.BitmapHolder
import kotlinx.parcelize.Parcelize

class ImageToImageFragment: AiBaseFragment<ImageToImageFragmentBinding>() {

    companion object{

        fun getInstance(args: Args = Args()) = ImageToImageFragment().apply {

            arguments = Bundle().apply {

                putParcelable("args", args)

            }

        }

    }


    private val args by lazy {  arguments?.getParcelable("args")?:Args()}


    @Parcelize
    data class Args(val fromFill:Boolean = false): Parcelable


    override val mBinding: ImageToImageFragmentBinding by lazy { ImageToImageFragmentBinding.inflate(layoutInflater) }

    override fun onReadyCreateView(savedInstanceState: Bundle?) {



    }

    override fun onReadyView(view: View, savedInstanceState: Bundle?) {

        if (args.fromFill){

            mBinding.topViewTxt.text = getString(R.string.upload_or_browse)

        }

        mBinding.galleryView.setOnClickListener {

            PhotoSelectActivity.goToAiPhotoActivity(requireActivity(),activityLauncher)

        }


        mBinding.dcRv.run {

            layoutManager = GridLayoutManager(requireContext(),1,RecyclerView.HORIZONTAL,false)

            adapter = DiscoverMoreAdapter()

        }

    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {

            if (activityResult.resultCode== PHOTO_SELECT_RQ){

                runCatching {

                    if (args.fromFill){

                        GenerativeFillActivity.goToGenerativeFillActivity(requireActivity(),activityResult.data?.data,activityLauncher)

                    }else {

                        CropActivity.goToAiCropActivity(requireActivity(), activityResult.data?.data, activityLauncher)

                    }
                }

            }else if (activityResult.resultCode == CROP_RQ){

                runCatching {

                    ImageToImageActivity.goToAiImgToImgActivity(requireActivity(), activityResult.data?.data, activityLauncher)

                }

            }
    }
}