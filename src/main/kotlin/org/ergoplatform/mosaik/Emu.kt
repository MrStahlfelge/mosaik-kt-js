package org.ergoplatform.mosaik

import kotlin.js.Date

object System {
    fun currentTimeMillis(): Long = Date().getMilliseconds().toLong()
}

val Any.javaClass: ClassName get() = ClassName("No classname on JS")

data class ClassName(val simpleName: String)