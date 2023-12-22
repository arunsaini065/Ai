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
    val name: String?=null

){

    companion object {

        val DUMMY = InspirationData(
            name = "",
            imageUrl = "https://fastly.picsum.photos/id/278/536/354.jpg?hmac=B3RGgunW6oirJoQEgt80to9HNb7oZqLut-4fFVVc9NM"
        )

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
