package com.rocks.ui.inspiration

import androidx.recyclerview.widget.DiffUtil.ItemCallback

data class InspirationData(val name:String,val url:String){

    companion object{

        val DUMMY = InspirationData(name = "","https://fastly.picsum.photos/id/278/536/354.jpg?hmac=B3RGgunW6oirJoQEgt80to9HNb7oZqLut-4fFVVc9NM")

        val DIFF = object :ItemCallback<InspirationData>(){

            override fun areItemsTheSame(oldItem: InspirationData, newItem: InspirationData): Boolean {

                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: InspirationData, newItem: InspirationData): Boolean {
                return oldItem==newItem
            }

        }

    }

}
