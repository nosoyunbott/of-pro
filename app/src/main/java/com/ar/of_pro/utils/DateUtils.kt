package com.ar.of_pro.utils

import java.text.SimpleDateFormat
import java.util.Date

class DateUtils {

companion object {
    fun GetFormattedDate(timestampString: String): String {

        try {
            // Parse the timestamp string to a Long
            val timestampLong = timestampString.toLong()

            // Convert the timestamp to a Date object
            val date = Date(timestampLong)

            // You can format and display the date as needed
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = dateFormat.format(date)
            return formattedDate;
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            println("Failed to parse timestamp: ${e.message}")
            return "DateError"
        }

    }

}
}