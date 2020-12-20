package com.hodinkee.hodinnews

import java.text.SimpleDateFormat
import java.util.*

object DateFormat {
    fun ymd(date: Date): String = SimpleDateFormat("yyy-MM-dd", Locale.getDefault()).format(date)
}