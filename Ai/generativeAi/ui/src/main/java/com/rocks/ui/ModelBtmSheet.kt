package com.rocks.ui

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.model.Model
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


        return _binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tmpList:ArrayList<Model> = ArrayList()
        tmpList.add(Model("https://content.delivery-asset.com/img/default/Notification/51dcc727-feb9-4fac-bd97-038acbba1d75.jpg","Baby"))
        tmpList.add(Model("https://content.delivery-asset.com/img/default/Notification/6f8a5568-4387-4a69-8006-95a1f22eba46.jpg","Sona"))
        tmpList.add(Model("https://content.delivery-asset.com/img/default/Notification/c1c2260c-e466-42a8-a910-4d88bb16f8b9.jpg","Babu"))
        tmpList.add(Model("https://content.delivery-asset.com/img/default/Notification/62aa258e-66d2-4e6f-af3d-cdfe83d1b573.jpg","Chiku"))
        tmpList.add(Model("https://content.delivery-asset.com/img/default/Notification/b51e426d-feb5-4c47-9d07-b74b267af305.jpg","Honey"))
        tmpList.add(Model("https://content.delivery-asset.com/img/default/Notification/c823eb77-a721-487f-82e5-2aa891dcb81b.jpg","Bebo"))

        _binding.modelRv.adapter = context?.let { ModelAdapter(it).apply {
            submitList(tmpList)
        } }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog


        return bottomSheetDialog
    }

}
