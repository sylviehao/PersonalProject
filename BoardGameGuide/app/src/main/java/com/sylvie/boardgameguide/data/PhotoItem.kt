package com.sylvie.boardgameguide.data

sealed class PhotoItem {

    abstract val id: Long

    data class FullItem(val event: String): PhotoItem() {
        override val id: Long
            get() = 0
    }
    data class EmptyItem(val event: String): PhotoItem() {
        override val id: Long
            get() = 1
    }
}