package com.chsteam.mypets.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.chsteam.mypets.api.config.quest.QuestPackage
import net.objecthunter.exp4j.ExpressionBuilder
import java.io.ByteArrayOutputStream
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


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

fun addPackage(pack: QuestPackage, string: String): String {
    return if (string.contains(".")) {
        string
    } else {
        pack.packName + "." + string
    }
}

@OptIn(ExperimentalEncodingApi::class)
fun convertImageToBase64(context: Context, assetFileName: String): String {
    val inputStream = context.assets.open(assetFileName)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encode(byteArray)
}

fun calculateExpression(expression: String, variables: Map<String, Double>): Double {
    val expressionBuilder = ExpressionBuilder(expression)
    variables.forEach { (variable, value) ->
        expressionBuilder.variable(variable)
    }
    val finalExpression = expressionBuilder.build()
    variables.forEach { (variable, value) ->
        finalExpression.setVariable(variable, value)
    }
    return finalExpression.evaluate()
}