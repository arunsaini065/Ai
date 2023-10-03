package com.rocks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.ui.databinding.BtmSheetSelectmodelBinding
import com.rocks.ui.databinding.ChooseSettingBtmsheetBinding

class SettingBtmSheet: BottomSheetDialogFragment() {

    private val _settingbinding by lazy { ChooseSettingBtmsheetBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = with(_settingbinding) {

//        modelRv.adapter = ModelAdapter()

        return _settingbinding.root

    }

}
