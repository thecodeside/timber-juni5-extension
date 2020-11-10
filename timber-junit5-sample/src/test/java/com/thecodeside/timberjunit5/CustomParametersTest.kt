package com.thecodeside.timberjunit5

import android.util.Log
import com.thecodeside.timberjunit5.TimberTestExtension.Rules
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class CustomParametersTest {

    @JvmField
    @RegisterExtension
    val testExtension = TimberTestExtension(
        Rules(
            minPriority = Log.ERROR,
            showThread = true,
            showTimestamp = true,
            logOnlyWhenTestFails = true
        )
    )

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}