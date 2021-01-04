package com.sylvie.boardgameguide

import com.sylvie.boardgameguide.tools.bottle.BottleFragment
import org.junit.Assert
import org.junit.Test


class BottlePossibilityUnitTest {

    var fragment = BottleFragment()

    @Test
    fun getPossibility() {
        Assert.assertEquals(270, fragment.possibility(20))
        Assert.assertEquals(150, fragment.possibility(57))
        Assert.assertEquals(60, fragment.possibility(85))

        Assert.assertNotEquals(180, fragment.possibility(33))
        Assert.assertNotEquals(330, fragment.possibility(1))
        Assert.assertNotEquals(30, fragment.possibility(77))
    }
}