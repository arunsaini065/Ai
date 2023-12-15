package com.rocks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class AiBaseFragment<B:ViewDataBinding> : Fragment() {


    abstract val mBinding : B

    abstract fun onReadyCreateView(savedInstanceState: Bundle?)

    abstract fun onReadyView(view: View,savedInstanceState: Bundle?)

    abstract fun onRegisterForActivityResult(activityResult: ActivityResult)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        onReadyCreateView(savedInstanceState)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        onReadyView(view, savedInstanceState)
    }


    val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {


        onRegisterForActivityResult(it)

    }

}