package com.rocks.ui.selectimg

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rocks.fetcher.AlbumModel
import com.rocks.ui.base.AiModelBaseAdapter
import com.rocks.ui.base.AiModelBaseHolder
import com.rocks.ui.toBinding

class FolderAdapter(val callback:(AlbumModel)->Unit): AiModelBaseAdapter<AlbumModel>(AlbumModel.DIFF) {

    override fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int): AiModelBaseHolder {

        return  FolderHolder(parent.toBinding())
    }

    override fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int) {

        if (holder is FolderHolder) {

           if (position==0){

               runCatching {

                   val item = getItem(1)

                   Glide.with(holder.itemView).load(item.bucketData).centerCrop().into(holder.binding.folderThumbnail)


                   holder.binding.name.text = getItem(position).bucketName

               }


           }else {

               val item = getItem(position)

               Glide.with(holder.itemView).load(item.bucketData).centerCrop().into(holder.binding.folderThumbnail)

               holder.binding.name.text = item.bucketName


               holder.itemView.setOnClickListener {

                   runCatching {

                       callback(getItem(position))

                   }

               }

           }

        }

    }
}