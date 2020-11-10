package com.thecodeside.timberjunit5

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


@ExtendWith(TimberTestExtension::class)
class DefaultParametersTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}