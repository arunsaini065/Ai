package com.rocks.ui.imageloader

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract  class AsyncTaskCoroutine<T>( private val delay: Long=0) : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job



       fun cancelTask() {
        job.cancel()
    }



    fun execute() = launch {
        MainScope()
        onPreExecute()
        val result = doSomeBackgroundWork()
        doUiStuff(result)
    }


    private suspend fun doSomeBackgroundWork(): T? = withContext(Dispatchers.IO) {

        val result = doInBackground()
        delay(delay)
        return@withContext result
    }

    private fun doUiStuff(result: T?) {
        Handler(Looper.getMainLooper()).post {
            onPostExecute( result)
        }
    }

    abstract   fun doInBackground():T?
    abstract fun onPreExecute()
    abstract   fun onPostExecute(result: T?)

}
