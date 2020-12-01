package com.sylvie.boardgameguide.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var id: String = "",
    var hostId: String = "",
    var topic: String = "",
    var image: MutableList<String>? = null,
    var time: Long = 0,
    var location: String = "",
    var gameId: String = "",
    var playerLimit: Int = 0,
    var playerList: MutableList<String>? = null,
    var message: MutableList<Message>? = null,
    var description: String = "",
    var rules: String = "",
    var status: String = "",
    var like: MutableList<String>? = null,
    var createdTime : com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
): Parcelable