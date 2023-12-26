package com.rocks.ui

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.bumptech.glide.Glide
import com.rocks.ui.base.AiModelBaseAdapter
import com.rocks.ui.base.AiModelBaseHolder

class OutputsAdapter: AiModelBaseAdapter<Bitmap>(DIFF) {



    companion object{

        val DIFF = object : ItemCallback<Bitmap>() {

            override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {

                return false
            }



            override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {

                return false

            }

        }

    }

    override fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int): AiModelBaseHolder {


        return OutputsHolder(parent.toBinding())

    }


    override fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int) {


        if (holder is OutputsHolder){


            Glide.with(holder.itemView).load(getItem(position)).centerCrop().into(holder.binding.thumbnail)


        }


    }

}