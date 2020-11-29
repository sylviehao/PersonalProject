package com.sylvie.boardgameguide.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: String = "",
    var eventId: String = "",
    var userId: String = "",
    var message: String = ""
): Parcelable