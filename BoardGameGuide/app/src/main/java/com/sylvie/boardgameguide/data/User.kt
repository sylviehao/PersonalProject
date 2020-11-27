package com.sylvie.boardgameguide.data

data class User(
    var id: String = "",
    var name: String = "",
    var introduction: String = "",
    var favorite: MutableList<Game>? = null,
    var browseRecently: MutableList<BrowseRecently>? = null
)

data class BrowseRecently(
    var gameId: String = "",
    var time: Long = 0
)