package com.example.androidchatapp.helper

import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*

class DateParser {
    companion object {
        fun parseTimestamp(timestamp: String, convertToDay: Boolean): String {
            try {
                val sdf: SimpleDateFormat
                val netDate = Date(Long.parseLong(timestamp) * 1000)

                if (convertToDay && netDate.day < Date().day) {
                    sdf = SimpleDateFormat("MM/dd/yyyy")
                } else {
                    sdf = SimpleDateFormat("HH:mm")
                }

                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }
    }
}