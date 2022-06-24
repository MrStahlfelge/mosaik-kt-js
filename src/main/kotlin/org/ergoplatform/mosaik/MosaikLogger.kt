package org.ergoplatform.mosaik

/**
 * MosaikLogger is used to log internal warnings, errors, and information by Mosaik.
 * Set [logger] to provide an own logging implementation that suits your needs, or
 * set the [DefaultLogger] to print to stdout.
 */
object MosaikLogger {

    fun logDebug(message: String, t: Throwable? = null) {
        logger?.invoke(Severity.Debug, message, t)
    }

    fun logInfo(message: String, t: Throwable? = null) {
        logger?.invoke(Severity.Info, message, t)
    }

    fun logWarning(message: String, t: Throwable? = null) {
        logger?.invoke(Severity.Warning, message, t)
    }

    fun logError(message: String, t: Throwable? = null) {
        logger?.invoke(Severity.Error, message, t)
    }

    var logger: ((Severity, String, Throwable?) -> Unit)? = null

    enum class Severity {
        Debug,
        Info,
        Warning,
        Error
    }

    val DefaultLogger: ((Severity, String, Throwable?) -> Unit) =
        { severity, msg, throwable ->
            println("$severity: $msg")
            throwable?.let { t ->
                println(t.javaClass.simpleName + " " + t.message)
                t.printStackTrace()
            }
        }
}