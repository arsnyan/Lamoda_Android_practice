package com.arsnyan.lamodacopy.utils

import android.icu.util.Calendar
import java.util.Date

fun Date.hasDaysPassed(days: Int): Boolean {
    val startCalendar = Calendar.getInstance().apply { time = this@hasDaysPassed }
    val currentCalendar = Calendar.getInstance()

    startCalendar.add(Calendar.DAY_OF_YEAR, days)

    return currentCalendar.after(startCalendar)
}