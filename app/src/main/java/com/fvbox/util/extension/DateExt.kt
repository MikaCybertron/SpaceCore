package com.fvbox.util.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @description:
 * @author: Jack
 * @create: 2022-06-12
 */

fun Date.str(): String {
    val simpleFormatter = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss", Locale.getDefault())
    return simpleFormatter.format(this)
}

fun nowStr(): String {
    val date = Date()
    return date.str()
}

fun Long.date(): String {
    val date = Date(this)
    return date.str()
}
