package com.rocks.model

import androidx.recyclerview.widget.DiffUtil

data class ModelListDataItem(
    val api_calls: String,
    val created_at: String,
    val description: String,
    val featured: String,
    val instance_prompt: String,
    val is_nsfw: String,
    val model_category: String,
    val model_id: String,
    val model_name: String,
    val screenshots: String,
    val status: String
){

    companion object{

        val DIFF = object : DiffUtil.ItemCallback<ModelListDataItem>() {
            override fun areItemsTheSame(oldItem: ModelListDataItem, newItem: ModelListDataItem): Boolean {
                return  oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: ModelListDataItem, newItem: ModelListDataItem): Boolean {
                return  oldItem==newItem
            }

        }
    }
}
