package org.ergoplatform.mosaik

class ElementNotFoundException(msg: String, val elementId: String) : IllegalArgumentException(msg)

class ChangeViewContentException(t: Throwable) : IllegalStateException("Error changing view content: ${t.message}", t)

class InvalidValuesException(msg: String, val errorList: String) : IllegalArgumentException(msg)

class ConnectionException(t: Throwable) : RuntimeException(t)