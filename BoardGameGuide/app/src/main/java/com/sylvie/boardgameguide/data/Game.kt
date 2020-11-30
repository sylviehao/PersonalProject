package com.sylvie.boardgameguide.data

import com.facebook.internal.Mutable

data class Game(
    var id: String = "",
    var name: String = "",
    var image: MutableList<String>? = null,
    var type: MutableList<String>? = null,
    var playerLimit: Int = 0,
    var time: Int = 0,
    var rules: String = "",
    var roles: MutableList<String>? = null
)