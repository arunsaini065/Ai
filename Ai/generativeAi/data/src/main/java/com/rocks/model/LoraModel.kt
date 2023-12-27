package com.rocks.model

import com.google.gson.annotations.SerializedName

data class LoraModel(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("url")
    val url: String
)
{



    fun toModelListDataItem(): ModelListDataItem {

        return ModelListDataItem(
            "", "",
            "", "",
            "", "", "", id, name, url, ""
        )
    }

}