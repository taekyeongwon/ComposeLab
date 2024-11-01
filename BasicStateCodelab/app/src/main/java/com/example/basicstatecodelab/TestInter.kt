package com.example.basicstatecodelab

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface TestInter {
    fun onTest()
}

@Stable
class TestImpl : TestInter {
    override fun onTest() {
        println("TestImpl")
    }
}