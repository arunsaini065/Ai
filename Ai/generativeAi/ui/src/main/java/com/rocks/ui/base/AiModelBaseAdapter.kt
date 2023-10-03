package com.rocks.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


abstract class AiModelBaseAdapter<T>(diffCallback: DiffUtil.ItemCallback<T> ): ListAdapter<T, AiModelBaseHolder>(diffCallback) {

    abstract fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int):AiModelBaseHolder

    abstract fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiModelBaseHolder {

        return onCreateViewHolderItem(parent, viewType)

    }

    override fun onBindViewHolder(holder: AiModelBaseHolder, position: Int) {
        onBindViewHolderItem(holder, position)
    }
}