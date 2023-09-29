package com.rocks.ui

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class AiBaseActivity<B: ViewDataBinding> :AppCompatActivity() {


     abstract val mBinding : B

     abstract fun onReadyActivity(savedInstanceState: Bundle?)

     abstract fun onRegisterForActivityResult(activityResult: ActivityResult)


     override fun onCreate(savedInstanceState: Bundle?) {

          super.onCreate(savedInstanceState)

          setContentView(mBinding.root)

          onReadyActivity(savedInstanceState)

     }


      val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {


           onRegisterForActivityResult(it)

     }


}