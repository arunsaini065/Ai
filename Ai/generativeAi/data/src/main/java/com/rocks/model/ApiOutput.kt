package com.rocks.model

data class ApiOutput(
    val generationTime: Double,
    val id: Long,
    val tip:String,
    val eta: Float,
    val meta: Meta,
    var output: MutableList<String>,
    val status: String,
    val webhook_status: String,
    val message: String,
    val fetch_result:String,
    val future_links:MutableList<String>
)