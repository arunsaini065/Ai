package com.rocks.ui.selectimg

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rocks.fetcher.MediaStoreData
import com.rocks.ui.base.AiModelBaseAdapter
import com.rocks.ui.base.AiModelBaseHolder
import com.rocks.ui.toBinding

class SelectImageAdapter(val callback:(MediaStoreData)->Unit) : AiModelBaseAdapter<MediaStoreData>(MediaStoreData.DIFF) {

    override fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int): AiModelBaseHolder {

        return SelectImageHolder(parent.toBinding())
    }

    override fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int) {

         if (holder is SelectImageHolder) {

             Glide.with(holder.itemView).load(getItem(position).uri).centerCrop().into(holder.binding.imageThumbnail)

         }

        holder.itemView.setOnClickListener {

            callback(getItem(holder.adapterPosition))

        }

    }
}