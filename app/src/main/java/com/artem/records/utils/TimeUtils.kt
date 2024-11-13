package com.artem.records.utils

object TimeUtils {
    init {
        System.loadLibrary("timestamp")
    }

    external fun getCurrentTimestamp(): Long
}
