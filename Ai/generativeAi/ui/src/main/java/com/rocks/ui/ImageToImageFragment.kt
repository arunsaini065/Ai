package com.rocks.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rocks.ui.databinding.ImageToImageFragmentBinding
import com.rocks.ui.discovermore.DiscoverMoreAdapter
import com.rocks.ui.selectimg.PhotoSelectActivity
import kotlinx.parcelize.Parcelize

class ImageToImageFragment: AiBaseFragment<ImageToImageFragmentBinding>() {

    companion object{

        fun getInstance(args: Args = Args()) = ImageToImageFragment().apply {

            arguments = Bundle().apply {

                putParcelable("person", args)

            }

        }

    }


    private val args by lazy {  arguments?.getParcelable("person")?:Args()}


    @Parcelize
    data class Args(val fromFill:Boolean = false): Parcelable


    override val mBinding: ImageToImageFragmentBinding by lazy { ImageToImageFragmentBinding.inflate(layoutInflater) }

    override fun onReadyCreateView(savedInstanceState: Bundle?) {



    }

    override fun onReadyView(view: View, savedInstanceState: Bundle?) {

        if (args.fromFill){

            mBinding.topViewTxt.text = getString(R.string.upload_or_browse)

        }

        mBinding.btnGenerate.setOnClickListener {

            PhotoSelectActivity.goToAiHomeActivity(requireActivity())

        }

        mBinding.dcRv.run {

            layoutManager = GridLayoutManager(requireContext(),2,RecyclerView.HORIZONTAL,false)

            adapter = DiscoverMoreAdapter().apply {

                submitList(getDummyIns())

            }

        }

    }

    override fun onRegisterForActivityResult(activityResult: ActivityResult) {

    }
}