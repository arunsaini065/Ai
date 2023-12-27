package com.rocks.ui.discovermore

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.rocks.InspirationData
import com.rocks.ui.toBinding

class DiscoverMoreAdapter: ListAdapter<InspirationData, DiscoverMoreHolder>(InspirationData.DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverMoreHolder {

        return DiscoverMoreHolder(parent.toBinding())

    }

    override fun getItemCount(): Int {
        return super.getItemCount()+1
    }

    override fun onBindViewHolder(holder: DiscoverMoreHolder, position: Int) {


        Glide.with(holder.itemView.context).load("https://img.rareprob.com/img/AI/img_example1.jpg").fitCenter().into(holder.binding.imageItem)


    }
}