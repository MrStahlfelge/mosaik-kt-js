package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.layout.Box

@Serializable
@SerialName("LazyLoadBox")
class LazyLoadBox : Box() {
    @SerialName("url")
    lateinit var requestUrl: String
    var errorView: ViewElement? = null
}