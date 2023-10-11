package com.rocks.ui
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rocks.model.ModelListDataItem
import com.rocks.ui.base.AiModelBaseAdapter
import com.rocks.ui.base.AiModelBaseHolder

class ModelAdapter(var callback:(ModelListDataItem)->Unit): AiModelBaseAdapter<ModelListDataItem>(ModelListDataItem.DIFF) {




    override fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int): AiModelBaseHolder {
        return  ModelHolder(parent.toBinding())


    }

    override fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int) {

        if(holder is ModelHolder){


            holder.binding.modelItmId.text = getItem(position).model_id

            Glide.with(holder.itemView.context)

                .load(getItem(position).screenshots)

                .into(holder.binding.modelImg)

            holder.itemView.setOnClickListener {

                callback(getItem(position))

            }

        }




    }

}
