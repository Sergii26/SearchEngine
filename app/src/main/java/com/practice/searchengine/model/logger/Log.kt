package com.practice.searchengine.model.logger

interface Log {
    fun log(message: String?): Logger?
    fun withCause(cause: Exception?)
}
