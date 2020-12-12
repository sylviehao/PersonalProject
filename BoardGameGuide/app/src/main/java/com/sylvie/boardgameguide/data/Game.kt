package com.sylvie.boardgameguide.data

import android.os.Parcelable
import com.facebook.internal.Mutable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    var id: String = "",
    var name: String = "",
    var image: MutableList<String>? = null,
    var type: MutableList<String>? = null,
    var playerLimit: Int = 0,
    var time: Long = 0,
    var rules: String = "",
    var roles: MutableList<String>? = null
): Parcelable {

}