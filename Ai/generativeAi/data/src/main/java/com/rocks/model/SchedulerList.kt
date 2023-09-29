package com.rocks.model

data class SchedulerList(
    val message: String,
    val status: String,
    val schedulers: MutableList<String>
)