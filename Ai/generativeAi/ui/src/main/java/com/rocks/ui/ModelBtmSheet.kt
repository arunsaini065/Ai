package com.rocks.ui

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.ui.databinding.BtmSheetSelectmodelBinding

class ModelBtmSheet: BottomSheetDialogFragment() {

    private val _binding by lazy { BtmSheetSelectmodelBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme);
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = with(_binding) {

        modelRv.adapter = ModelAdapter()

        return _binding.root

    }


}
