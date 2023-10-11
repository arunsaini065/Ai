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

     _settingBinding.seedTxt.addTextChangedListener {

            if (::onBodyHandlerListener.isInitialized){

                onBodyHandlerListener.getHandlerBody().seed = it.toString().toInt()

            }

        }

        _settingBinding.stepsSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                if (::onBodyHandlerListener.isInitialized){

                    onBodyHandlerListener.getHandlerBody().numbInferenceSteps = seekBar?.progress.toString()

                }

            }
        })

        _settingBinding.cfgSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                if (::onBodyHandlerListener.isInitialized){

                    onBodyHandlerListener.getHandlerBody().guidanceScale = seekBar?.progress?.toDouble()


                }

            }
        })

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {


        return _settingBinding.root

    }

}
