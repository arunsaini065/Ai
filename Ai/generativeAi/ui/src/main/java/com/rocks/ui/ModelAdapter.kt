package com.rocks.ui
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rocks.model.ModelListDataItem
import com.rocks.ui.base.AiModelBaseAdapter
import com.rocks.ui.base.AiModelBaseHolder

class ModelAdapter(private var isStyle:Boolean, var callback:(ModelListDataItem?)->Unit): AiModelBaseAdapter<ModelListDataItem>(ModelListDataItem.DIFF) {


    var isModelId:String? = ""

    companion object{

         const val HEADER = -1

         const val NO_EFFECT = -2

    }

    override fun getItemCount(): Int {

        return if (isStyle){

            super.getItemCount()+2


        }else{

            super.getItemCount()+1

        }

    }

    override fun getItemViewType(position: Int): Int {

        if (position==0){

            return HEADER

        } else if (isStyle && position==1){

            return NO_EFFECT
        }

        return super.getItemViewType(position)


    }


    override fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int): AiModelBaseHolder {
        return when (viewType) {
            HEADER -> {

                ModelTopIHolder(parent.toBinding())

            }
            NO_EFFECT -> {

                return NoEffectHolder(parent.toBinding())
            }

            else -> {

                ModelHolder(parent.toBinding())

            }
        }

    }

    private fun getNewItem(position: Int) = getItem(getNewPosition(position))

    private fun getNewPosition(position: Int): Int {

       var  pos = if (isStyle) position-2 else position-1

        if (pos<0){

            pos = 0

        }

        return pos

    }

    override fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int) {

        if(holder is ModelHolder){

            val item = getNewItem(position)

            holder.binding.modelItmId.text = item.model_id

            Glide.with(holder.itemView.context)

                .load(item.screenshots)

                .into(holder.binding.modelImg)

            holder.itemView.setOnClickListener {

                callback(getNewItem(position))

            }

            if (isModelId.isNullOrEmpty().not() && isModelId?.contains(item.model_id) == true){

                holder.binding.selected.setBackgroundResource(R.drawable.bg_boarder_selected)

            }else{

                holder.binding.selected.setBackgroundResource(0)


            }

        }else if (holder is NoEffectHolder){

              holder.itemView.setOnClickListener {

                  callback(null)

              }

             if (isModelId.isNullOrEmpty()){

                 holder.binding.effectMain.setBackgroundResource(R.drawable.bg_boarder_selected)

             }else{

                 holder.binding.effectMain.setBackgroundResource(0)

             }

        }




    }

}
