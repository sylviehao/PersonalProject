package com.sylvie.boardgameguide.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var image: String = "",
    var introduction: String = "",
    var favorite: MutableList<Game>? = null,
    var browseRecently: MutableList<BrowseRecently>? = null
): Parcelable{

}

