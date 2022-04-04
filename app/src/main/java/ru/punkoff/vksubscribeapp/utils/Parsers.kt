package ru.punkoff.vksubscribeapp.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


/**
 * Parser for numbers
 * 1-9999: return count
 * 10 000-999 999: return format 124K, for example
 * 1 000 000-endless: return format 124KK, for example
 */
fun parseCount(count: Int?): String {
    var res = count.toString()
    count?.let {
        var N = count
        var i = 0
        while (N > 0) {
            N /= 10
            ++i
        }

        if (i in 5..6) {
            res = "${count / 1000}K"
        } else if (i > 6) {
            res = "${count / 1000000}KK"
        }
    }
    return res
}

fun parseLongToDate(time: Long?): String {
    val locale = Locale("ru")
    val simpleDateFormat = SimpleDateFormat("d MMMM", locale)
    Log.e("parseToDate", time.toString())
    return simpleDateFormat.format(time)
}