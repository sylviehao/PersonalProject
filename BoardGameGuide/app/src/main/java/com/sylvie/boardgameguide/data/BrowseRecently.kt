package com.sylvie.boardgameguide.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BrowseRecently(
    var gameId: String = "",
    var time: Long = 0
): Parcelable