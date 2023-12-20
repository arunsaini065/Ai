package com.rocks.ui.selectimg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rocks.fetcher.MediaViewModel
import com.rocks.ui.R
import com.rocks.ui.databinding.FolderViewItemBinding
import kotlinx.coroutines.launch

class FolderBottomSheet : BottomSheetDialogFragment() {

    private val _binding by lazy { FolderViewItemBinding.inflate(layoutInflater) }

    private val  _viewModel by lazy { ViewModelProvider(requireActivity())[MediaViewModel::class.java] }

    private val _folderAdapter by lazy {

        FolderAdapter {

              dismiss()

            _viewModel.fetchDataByBuket(it)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding.rvAlbum.adapter = _folderAdapter



        lifecycleScope.launch {

            _viewModel.listOfFolder.collect{

                _folderAdapter.submitList(it)

            }

        }


        return _binding.root

    }


}