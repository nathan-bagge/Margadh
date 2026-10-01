package org.setu.margadh.models

data class MarketModel(
    var id: Long = 0L,
    var name: String = "",
    var description: String = "",
    var county: String = "",
    var openingDays: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0
)