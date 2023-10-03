package com.rocks.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rocks.model.Model
import com.rocks.ui.base.AiModelBaseAdapter
import com.rocks.ui.base.AiModelBaseHolder

class ModelAdapter: AiModelBaseAdapter<Model>(Model.DIFF) {


    override fun onCreateViewHolderItem(parent: ViewGroup, viewType: Int): AiModelBaseHolder {
        return  ModelHolder(parent.toBinding())


    }

    override fun onBindViewHolderItem(holder: AiModelBaseHolder, position: Int) {



    }

}
