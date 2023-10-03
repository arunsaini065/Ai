package com.rocks.model

import androidx.recyclerview.widget.DiffUtil

data class Model(
    val load_data: String,
    val model_id: String,
){
    companion object{
        val DIFF = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return  oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return  oldItem==newItem
            }

        }
    }
}