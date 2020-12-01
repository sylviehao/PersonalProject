package com.sylvie.boardgameguide.data

sealed class HomeItem {

    abstract val id: Long

    data class PostItem(val event: Event): HomeItem() {
        override val id: Long
            get() = event.id.toLong()
    }
    data class EventItem(val event: Event): HomeItem() {
        override val id: Long
            get() = event.id.toLong()
    }
}