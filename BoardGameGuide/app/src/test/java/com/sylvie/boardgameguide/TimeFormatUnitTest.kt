package com.sylvie.boardgameguide

import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TimeFormatUnitTest {

    private val day = (1000L * 60 * 60 * 24)
    private val hour = (1000L * 60 * 60)
    private val minute = (1000L * 60)
    private val second = 1000L

    private fun getTime(currentTime: Long, date: Long): String {
        val timeDiff = currentTime - date

        return when {
            timeDiff > day -> {
                "${(timeDiff / day)}天前"
            }
            timeDiff in (hour + 1) until day -> {
                "${(timeDiff / hour)}小時前"
            }
            else -> {
                "${(timeDiff / minute)}分鐘前"
            }
        }
    }

    @Test
    fun checkTime() {
        assertEquals("1分鐘前", getTime(222 * second, 111 * second))
        assertEquals("7小時前", getTime(14 * hour, 7 * hour))
        assertEquals("3天前", getTime(4 * day, 1 * day))
        assertEquals("59分鐘前", getTime(60 * minute, 59 * second))
        assertNotEquals("24分鐘前", getTime(48 * minute, 111 * second))
        assertNotEquals("4小時前", getTime(15 * hour, 661 * minute))
    }

}