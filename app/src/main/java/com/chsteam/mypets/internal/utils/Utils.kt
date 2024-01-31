package com.chsteam.mypets.internal.utils

import java.util.regex.Matcher
import java.util.regex.Pattern


fun split(string: String): Array<String> {
    val list: MutableList<String> = ArrayList()
    val matcher: Matcher = Pattern.compile("(?:(?:(\\S*)(?:\")([^\"]*?)(?:\"))|(\\S+))\\s*").matcher(string)
    while (matcher.find()) {
        if (matcher.group(3) == null) {
            list.add(matcher.group(1) + matcher.group(2))
        } else {
            list.add(matcher.group(3))
        }
    }
    return list.toTypedArray()
}