package com.rocks

import com.rocks.model.ApiOutput

enum class OutPutSingleton {

    INSTANCE;

    private lateinit var output:ApiOutput

    companion object{


        fun setOutput(output: ApiOutput?){

            if (output != null) {

                INSTANCE.output=output

            }

        }

        fun hasOutput() = INSTANCE::output.isInitialized


        fun getOutPut() = if (hasOutput()){

            INSTANCE.output

        }else{

            null
        }


    }

}
