package com.example.nearme.Models.test

data class PlacesModelList(
    val debug_log: DebugLog,
    val html_attributions: List<Any>,
    val logging_info: LoggingInfo,
    val results: List<Result>,
    val status: String
)