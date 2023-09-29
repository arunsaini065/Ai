package com.rocks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class AiBaseActivity<B: ViewDataBinding> :AppCompatActivity() {


     abstract val mBinding : B

     abstract fun onReadyActivity(savedInstanceState: Bundle?)


     override fun onCreate(savedInstanceState: Bundle?) {

          super.onCreate(savedInstanceState)

          setContentView(mBinding.root)

          onReadyActivity(savedInstanceState)

     }


}