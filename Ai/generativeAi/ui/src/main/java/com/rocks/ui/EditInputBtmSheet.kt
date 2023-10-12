package com.rocks.ui


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.OnBodyHandlerListener
import com.rocks.OnGeneratorListener
import com.rocks.ui.databinding.EditInBtmsheetBinding

class EditInputBtmSheet: BottomSheetDialogFragment() {

    private lateinit var onBodyHandlerListener: OnBodyHandlerListener

    private lateinit var onGeneratorListener: OnGeneratorListener

    private lateinit var onCancelFragment: OnCancelFragment

    private val _binding by lazy { EditInBtmsheetBinding.inflate(layoutInflater) }

    override fun onCancel(dialog: DialogInterface) {

        if (::onCancelFragment.isInitialized){

            onCancelFragment.onCancel()

        }

        super.onCancel(dialog)

    }

    override fun dismiss() {

        if (::onCancelFragment.isInitialized){

            onCancelFragment.onCancel()

        }

        super.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme)

    }

    override fun onAttach(context: Context) {

        if (context is OnBodyHandlerListener){

            onBodyHandlerListener = context

        }

        if (context is OnGeneratorListener){

            onGeneratorListener = context

        }

        if (context is OnCancelFragment){

            onCancelFragment = context

        }

        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = _binding.root



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)



        _binding.editTxt.addTextChangedListener {

            if (::onBodyHandlerListener.isInitialized){

                onBodyHandlerListener.getHandlerBody().positivePrompt = it.toString()

            }

        }

        _binding.slctModel.setOnClickListener {

            activity?.supportFragmentManager?.let { it1 -> ModelBtmSheet().apply {

                callback = object :OnCancelFragment{

                    override fun onCancel() {

                        if (::onBodyHandlerListener.isInitialized){

                            _binding.slctModel.text = onBodyHandlerListener.getHandlerBody().modelId


                        }
                    }

                }

            }.show(it1,"") }

        }

        _binding.generate.setOnClickListener {

            dismiss()

            if (::onGeneratorListener.isInitialized) {

                onGeneratorListener.onGenerator()

            }

        }



        _binding.chSetting.setOnClickListener {

            activity?.supportFragmentManager?.let { it1 -> SettingBtmSheet().show(it1,"") }


        }
    }


}

