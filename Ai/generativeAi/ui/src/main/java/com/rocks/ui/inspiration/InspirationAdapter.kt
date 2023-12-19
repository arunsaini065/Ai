package com.rocks.ui.inspiration

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.rocks.ui.toBinding

class InspirationAdapter : ListAdapter<InspirationData, InspirationHolder>(InspirationData.DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationHolder {

        return InspirationHolder(parent.toBinding())

    }

    override fun onBindViewHolder(holder: InspirationHolder, position: Int) {


        Glide.with(holder.itemView.context).load(getItem(position).url).centerCrop().into(holder.binding.imageItem)


    }

}