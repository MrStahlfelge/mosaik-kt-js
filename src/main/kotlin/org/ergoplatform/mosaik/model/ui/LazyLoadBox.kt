package org.ergoplatform.mosaik.model.ui

import org.ergoplatform.mosaik.model.ui.layout.Box

/**
 * LazyLoad shows a loading indicator placeholder and fetches a [ViewElement] that replaces
 * the LazyLoadBox element when successfully loaded.
 * Best to be used with [LoadingIndicator].
 *
 *
 * In difference to [BackendRequestAction], this one
 * always makes a GET request to the given URL and does not disable user input. It is applicable
 * for fetching informational data that is not mandatory. The request response must be a
 * [ViewElement]
 */
class LazyLoadBox : Box() {
    lateinit var requestUrl: String
    var errorView: ViewElement? = null
}