package com.rocks.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.AspectRatio
import com.rocks.OnBodyHandlerListener
import com.rocks.ui.databinding.ChooseSettingBtmsheetBinding
import com.rocks.ui.ratio.CropRatioRecyclerView
import kotlinx.coroutines.launch

class SettingBtmSheet: BottomSheetDialogFragment() {

    companion object{

        const val CGF_SCALE_MIN =  10

        const val SEED_SCALE_MIN =  1

        const val STEP_SCALE_MIN =  11

    }

    override fun onCancel(dialog: DialogInterface) {

        if (::onCancelFragment.isInitialized){

            onCancelFragment.onCancel()

        }

        super.onCancel(dialog)

    }

    private lateinit var onBodyHandlerListener: OnBodyHandlerListener

    private lateinit var onCancelFragment: OnCancelFragment



    private val _settingBinding by lazy { ChooseSettingBtmsheetBinding.inflate(layoutInflater) }

    private val _aiUiViewModel by activityViewModels<AiUiViewModel>()


    override fun onCreate(savedInstanceState: Bundle?)  {

        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme)

        _settingBinding.seek1.cfgSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                var p = progress

                if (progress<= CGF_SCALE_MIN){
                    p = CGF_SCALE_MIN
                }

                if (::onBodyHandlerListener.isInitialized){

                    onBodyHandlerListener.getHandlerBody().guidanceScale = p.toDouble().div(10)

                    _settingBinding.seek1.cgfCount.text = "${onBodyHandlerListener.getHandlerBody().guidanceScale}"


                }
            }
        })
        _settingBinding.seek2.stepsSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {


                val p = progress+STEP_SCALE_MIN


                if (::onBodyHandlerListener.isInitialized){

                    onBodyHandlerListener.getHandlerBody().numbInferenceSteps = p.toString()

                    _settingBinding.seek2.stepsCount.text = "${onBodyHandlerListener.getHandlerBody().numbInferenceSteps}"


                }
            }
        })



        if (::onBodyHandlerListener.isInitialized) {

            with(onBodyHandlerListener.getHandlerBody()){

                _settingBinding.seek1.cfgSeekbar.progress = (guidanceScale?.times(10.00))?.toInt()?:0

                _settingBinding.seek2.stepsSeekbar.progress = numbInferenceSteps?.toInt()?:0


            }

        }

        _settingBinding.negativePrompt.addTextChangedListener {

            if (::onBodyHandlerListener.isInitialized){

                onBodyHandlerListener.getHandlerBody().negativePrompt = it.toString()

            }

        }

        lifecycleScope.launch {

            _aiUiViewModel.ratioUpdate.collect{

                _settingBinding.aspectRatioRvStng.notifySelectedItem(it,lifecycleScope)

            }
        }




        _settingBinding.aspectRatioRvStng.iChangeRatioListener = object :CropRatioRecyclerView.IChangeRatioListener{

            override fun onChangeRatio(width: Int, height: Int) {

                if (::onBodyHandlerListener.isInitialized){

                    onBodyHandlerListener.getHandlerBody().aspectRatio = AspectRatio(widthR = width, heightR = height)

                    _aiUiViewModel.ratioUpdate.value = onBodyHandlerListener.getHandlerBody()

                }

            }


        }

    }

    override fun onAttach(context: Context) {

        if (context is OnBodyHandlerListener){

            onBodyHandlerListener = context

        }
        if (context is OnCancelFragment) {

            onCancelFragment = context

        }

        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View  {


        return _settingBinding.root

    }

}
