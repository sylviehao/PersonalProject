package com.sylvie.boardgameguide.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: String = "",
    var hostId: String = "",
    var user: User? = null,
    var message: String = "",
    var createdTime : com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
): Parcelable