package com.rocks.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.ui.databinding.EditInBtmsheetBinding

class EditInputBtmSheet: BottomSheetDialogFragment() {

    private val _binding by lazy { EditInBtmsheetBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = with(_binding) {

//        modelRv.adapter = ModelAdapter()

        return _binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.slctModel.setOnClickListener {

            var btsheet = ModelBtmSheet()
            activity?.supportFragmentManager?.let { it1 -> btsheet.show(it1, "") }
        }



        _binding.chSetting.setOnClickListener {
            var btsheet = SettingBtmSheet()
            activity?.supportFragmentManager?.let { it1 -> btsheet.show(it1, "") }

        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog


        return bottomSheetDialog
    }

}

