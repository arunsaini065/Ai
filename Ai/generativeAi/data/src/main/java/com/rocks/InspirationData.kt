package com.rocks

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.google.gson.annotations.SerializedName

data class InspirationData(

    @SerializedName("description")
    val description: String?=null,

    @SerializedName("image_url")
    val imageUrl: String?=null,

    @SerializedName("model_id")
    val modelId: String?=null,

    @SerializedName("name")
    val name: String?=null,
    @SerializedName("type")
    val type: Int

){

    companion object {

        val DUMMY = InspirationData(
            name = "",
            imageUrl = "https://img.rareprob.com/img/AI/img_example1.jpg"
       , type = 0 )

        val DIFF = object : ItemCallback<InspirationData>() {

            override fun areItemsTheSame(
                oldItem: InspirationData,
                newItem: InspirationData
            ): Boolean {

                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: InspirationData,
                newItem: InspirationData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}
