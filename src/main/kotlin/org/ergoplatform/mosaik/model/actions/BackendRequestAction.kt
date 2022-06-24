package org.ergoplatform.mosaik.model.actions

/**
 * Makes a Request to URL. If postDataValues is true, it is a POST request and the body contains all
 * data values of the current view.
 *
 *
 * User data input is disabled while the action runs. If you only want to fetch informational data
 * and don't need to lock the user input, consider using
 * [org.ergoplatform.mosaik.model.ui.LazyLoadBox]
 *
 *
 * Response can result in any other action.
 */
class BackendRequestAction : UrlAction() {
    var postValues = PostValueType.ALL

    enum class PostValueType {
        /**
         * sends all data values, enforces validity before backend requests is executed
         */
        ALL,

        /**
         * sends valid data values, ignores invalid data
         */
        VALID,

        /**
         * does not send any data values, ignores invalid data
         */
        NONE
    }
}