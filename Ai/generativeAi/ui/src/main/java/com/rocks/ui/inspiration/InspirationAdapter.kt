package com.rocks.ui.inspiration

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.rocks.InspirationData
import com.rocks.ui.simplecropview.callback.CropCallback
import com.rocks.ui.toBinding

class InspirationAdapter(val callback:(InspirationData)->Unit) : ListAdapter<InspirationData, InspirationHolder>(InspirationData.DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationHolder {

        return InspirationHolder(parent.toBinding())

    }

    override fun onBindViewHolder(holder: InspirationHolder, position: Int) {


        Glide.with(holder.itemView.context).load(getItem(position).imageUrl).centerCrop().into(holder.binding.imageItem)


        holder.itemView.setOnClickListener {

            callback(getItem(holder.adapterPosition))

        }


    }

}