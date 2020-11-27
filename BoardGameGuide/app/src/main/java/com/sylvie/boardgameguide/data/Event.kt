package com.sylvie.boardgameguide.data

data class Event(
    var hostId: String = "",
    var topic: String = "",
    var image: MutableList<String>? = null,
    var time: Long = 0,
    var location: String = "",
    var playerLimit: Int = 0,
    var playerList: MutableList<String>? = null,
    var description: String = "",
    var rules: String = "",
    var status: String = ""
)