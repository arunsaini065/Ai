package com.rocks

import com.rocks.model.ApiOutput

enum class OutPutSingleton {

    INSTANCE;

    private lateinit var output:ApiOutput

    private lateinit var bodyHandler:BodyDataHandler

    companion object{


        fun setBodyHandler(bodyHandler:BodyDataHandler?){

            if (bodyHandler != null) {

                INSTANCE.bodyHandler = bodyHandler

            }

        }


        fun setOutput(output: ApiOutput?){

            if (output != null) {

                INSTANCE.output=output

            }

        }

        fun hasOutput() = INSTANCE::output.isInitialized

        fun hasBodyHandler() = INSTANCE::bodyHandler.isInitialized

        fun getBodyHandler() = if (hasBodyHandler()){

            INSTANCE.bodyHandler

        }else{

            null
        }



        fun getOutPut() = if (hasOutput()){

            INSTANCE.output

        }else{

            null
        }


    }

}
