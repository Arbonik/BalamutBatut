package com.castprogramms.balamutbatut

import com.castprogramms.balamutbatut.graph.Node
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val nodes = listOf(Node(mutableListOf()))
        val nodes1 = listOf(Node(mutableListOf()))
        assertArrayEquals((nodes.minus(nodes1)).toTypedArray(), arrayOf())
    }
}