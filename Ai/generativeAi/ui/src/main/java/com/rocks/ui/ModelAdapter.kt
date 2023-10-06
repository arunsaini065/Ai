package com.rocks.ui
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rocks.model.Model
import com.rocks.ui.base.AiModelBaseAdapter
import com.rocks.ui.base.AiModelBaseHolder

class ModelAdapter(var mContext:Context): AiModelBaseAdapter<Model>(Model.DIFF) {




    override fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int): AiModelBaseHolder {
        return  ModelHolder(parent.toBinding())


    }

    override fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int) {
        if(holder is ModelHolder){
//            holder.binding.modelImg
            holder.binding.modelItmId.text = getItem(position).model_id
            Glide.with(mContext)
                .load(getItem(position).load_data)
                .into(holder.binding.modelImg)
        }







    }

}
