package com.rocks.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.OnBodyHandlerListener
import com.rocks.stripHtml
import com.rocks.ui.databinding.BtmSheetSelectmodelBinding
import com.rocks.viewmodel.AiViewModel
import kotlinx.coroutines.launch

class ModelBtmSheet: BottomSheetDialogFragment() {

    private lateinit var onCancelFragment: OnCancelFragment


    override fun onCancel(dialog: DialogInterface) {

        if (::onCancelFragment.isInitialized){

            onCancelFragment.onCancel()

        }

        super.onCancel(dialog)

    }

    override fun onDismiss(dialog: DialogInterface) {

        if (::onCancelFragment.isInitialized){

            onCancelFragment.onCancel()

        }

        super.onDismiss(dialog)

    }

    private val _binding by lazy { BtmSheetSelectmodelBinding.inflate(layoutInflater) }

    private lateinit var onBodyHandlerListener: OnBodyHandlerListener

    private val _viewModel  by activityViewModels<AiViewModel>()

    private val _aiUiViewModel  by activityViewModels<AiUiViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme)

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
    ): View {

           lifecycleScope.launch {

               _viewModel.stateflowAiModelList.collect{ list->

                   _binding.modelRv.adapter = context?.let { ModelAdapter {


                       if (::onBodyHandlerListener.isInitialized){


                           onBodyHandlerListener.getHandlerBody().modelId = it.model_id

                           _aiUiViewModel.styleUpdate.value = onBodyHandlerListener.getHandlerBody()

                           dismiss()

                       }


                   }.apply {
                       submitList(list.data)
                   } }
               }

           }


        return _binding.root

    }

}
