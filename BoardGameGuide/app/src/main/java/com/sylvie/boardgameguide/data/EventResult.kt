package com.sylvie.boardgameguide.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventResult(
    val eventList: List<Event>? = null
) : Parcelable {
    fun toHomeItems(): List<HomeItem> {
        val items = mutableListOf<HomeItem>()

        eventList?.let {
                for (event in it) {
                    when (event.status) {
                        "OPEN" -> items.add(HomeItem.PostItem(event))
                        "CLOSE" -> items.add(HomeItem.EventItem(event))
                    }
                }
        }
        return items
    }

}