package com.chsteam.mypets.core

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.id.ConditionID
import com.chsteam.mypets.core.id.EventID
import com.chsteam.mypets.core.id.ID
import com.chsteam.mypets.core.id.ObjectiveID
import com.chsteam.mypets.core.utils.split
import java.util.regex.Pattern


class Instruction(val pack: QuestPackage, val identifier: ID, val instruction: String) {

    private val parts: Array<String> = split(instruction)
    private var nextIndex = 1
    private var currentIndex = 1
    private var lastOptional: String? = null

    companion object {
        private val NUMBER_PATTERN: Pattern = Pattern.compile("(?:\\s|\\G|^)(([+\\-])?\\d+)(?:\\s|$)")
    }

    override fun toString(): String {
        return instruction
    }

    fun size(): Int {
        return parts.size
    }

    fun copy(): Instruction {
        return Instruction(pack, identifier, instruction)
    }

    fun copy(newID: ObjectiveID): Instruction {
        return Instruction(pack, newID, instruction)
    }

    /////////////////////
    ///    GENERAL    ///
    /////////////////////

    operator fun hasNext(): Boolean {
        return currentIndex < parts.size - 1
    }

    operator fun next(): String? {
        lastOptional = null
        currentIndex = nextIndex
        return getPart(nextIndex++)
    }

    fun current(): String? {
        lastOptional = null
        currentIndex = nextIndex - 1
        return getPart(currentIndex)
    }

    fun getPart(index: Int): String? {
        if (parts.size <= index) {
            return null
            // TODO SHOULD BE Exception Not enough arguments
        }
        lastOptional = null
        currentIndex = index
        return parts[index]
    }

    fun getOptional(prefix: String): String? {
        return getOptionalArgument(prefix)
    }

    fun getOptional(prefix: String, defaultString: String): String {
        return getOptionalArgument(prefix) ?: defaultString
    }

    fun getOptionalArgument(prefix: String): String? {
        for (part in parts) {
            if (part.lowercase().startsWith(prefix.lowercase() + ":")) {
                lastOptional = prefix
                currentIndex = -1
                return part.substring(prefix.length + 1)
            }
        }
        return null
    }

    fun hasArgument(argument: String?): Boolean {
        for (part in parts) {
            if (part.equals(argument, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    ///////////////////
    ///    Enums    ///
    ///////////////////

    fun <T : Enum<T>> getEnum(clazz: Class<T>): T? = getEnum(next(), clazz)

    fun <T : Enum<T>> getEnum(string: String?, clazz: Class<T>): T? = getEnum(string, clazz, null)

    fun <T : Enum<T>> getEnum(string: String?, clazz: Class<T>, defaultValue: T?): T? {
        if (string == null) {
            return defaultValue
        }
        return try {
            java.lang.Enum.valueOf(clazz, string.uppercase())
        } catch (e: IllegalArgumentException) {
            return null
        }
    }


    /////////////////
    ///    IDs    ///
    /////////////////
    fun getEvent(): EventID? {
        return getEvent(next())
    }

    fun getEvent(string: String?): EventID? {
        return if (string == null) {
            null
        } else {
            EventID(pack, string)
        }
    }

    fun getCondition(): ConditionID? {
        return getCondition(next())
    }

    fun getCondition(string: String?): ConditionID? {
        return if (string == null) {
            null
        } else {
            ConditionID(pack, string)
        }
    }

    fun getObjective(): ObjectiveID? {
        return getObjective(next())
    }

    fun getObjective(string: String?): ObjectiveID? {
        return if (string == null) {
            null
        } else {
            ObjectiveID(pack, string)
        }
    }

    /////////////////////
    ///    NUMBERS    ///
    /////////////////////
    fun getByte(): Byte {
        return getByte(next(), 0.toByte())
    }

    fun getByte(string: String?, def: Byte): Byte {
        return string?.toByte() ?: def
    }

    fun getPositive(): Int {
        return getPositive(next(), 0)
    }

    fun getPositive(string: String?, def: Int): Int {
        val number = getInt(string, def)
        if (number <= 0) {
            return 0
            // TODO Throw ERROR
        }
        return number
    }

    fun getInt(): Int {
        return getInt(next(), 0)
    }

    fun getInt(string: String?, def: Int): Int {
        return string?.toInt() ?: def
    }

    fun getLong(): Long {
        return getLong(next(), 0)
    }

    fun getLong(string: String?, def: Long): Long {
        return string?.toLong() ?: def
    }

    fun getDouble(): Double {
        return getDouble(next(), 0.0)
    }

    fun getDouble(string: String?, def: Double): Double {
        return string?.toDouble() ?: def
    }

    fun getAllNumbers(): List<Int> {
        val matcher = NUMBER_PATTERN.matcher(instruction)
        val result = ArrayList<Int>()
        while (matcher.find()) {
            result.add(matcher.group(1).toInt())
        }
        return result
    }

    ////////////////////
    ///    ARRAYS    ///
    ////////////////////
    fun getArray(): Array<String?> {
        return getArray(next())
    }

    fun getArray(string: String?): Array<String?> {
        return string?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            ?: arrayOfNulls(0)
    }


    fun <T> getList(converter: Converter<T>): List<T>? {
        return getList(next(), converter)
    }

    fun <T> getList(string: String?, converter: Converter<T>): List<T> {
        if (string == null) {
            return ArrayList(0)
        }
        val array = getArray(string)
        val list: MutableList<T> = ArrayList(array.size)
        for (part in array) {
            list.add(converter.convert(part))
        }
        return list
    }

    /////////////////////////
    ///    OTHER STUFF    ///
    /////////////////////////
    interface Converter<T> {
        fun convert(string: String?): T
    }
}