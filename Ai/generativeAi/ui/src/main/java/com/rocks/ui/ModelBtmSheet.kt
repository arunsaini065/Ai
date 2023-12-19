package com.rocks.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.OnBodyHandlerListener
import com.rocks.model.ModelListDataItem
import com.rocks.ui.databinding.BtmSheetSelectmodelBinding
import com.rocks.uistate.ModelUiState
import com.rocks.viewmodel.AiViewModel
import kotlinx.coroutines.launch


class ModelBtmSheet: BottomSheetDialogFragment() {

    private lateinit var onCancelFragment: OnCancelFragment


      var callback: OnCancelFragment?=null


    private val isStyle by lazy { arguments?.getBoolean("is_style")?:false }



    override fun onCancel(dialog: DialogInterface) {

        if (::onCancelFragment.isInitialized){

            onCancelFragment.onCancel()

        }
        callback?.onCancel()

        super.onCancel(dialog)

    }

    override fun onDismiss(dialog: DialogInterface) {

        if (::onCancelFragment.isInitialized){

            onCancelFragment.onCancel()

        }
        callback?.onCancel()

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

               if (isStyle) {


                   loadModelAdapter(_viewModel.getLocalLoRAId())


               } else {

                   _viewModel.stateflowAiModelList.collect { list ->

                       loadModelAdapter(list)

                   }

               }

           }
        if (isStyle){

            _binding.textView.text = getString(R.string.select_style)

        }else{

            _binding.textView.text = getString(R.string.select_effects)

        }
        return _binding.root

    }

    private fun loadModelAdapter(list: ModelUiState<MutableList<ModelListDataItem>>) {

        (_binding.modelRv.layoutManager as GridLayoutManager).spanSizeLookup = object : SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {

                return when (_binding.modelRv.adapter?.getItemViewType(position)) {

                    ModelAdapter.HEADER -> 3

                    else -> 1

                }

            }

        }

        _binding.modelRv.adapter = ModelAdapter(isStyle) {


            if (::onBodyHandlerListener.isInitialized) {


                if (isStyle) {

                    onBodyHandlerListener.getHandlerBody().loraModel = it.model_id

                }else{

                    onBodyHandlerListener.getHandlerBody().modelId = it.model_id

                }

                _aiUiViewModel.modelUpdate.value = onBodyHandlerListener.getHandlerBody()

                dismiss()

            }


        }.apply {
            submitList(list.data)
        }
    }

}
