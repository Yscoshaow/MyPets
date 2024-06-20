package com.chsteam.mypets.core.compatibility.dungeonlab.mypets.data

import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.DataOverflowException

data class WaveData(val wave: ByteArray)  {
    data class Frequency(val data: ByteArray) {
        constructor(a: Int, b: Int, c: Int, d: Int) : this(data = ByteArray(4)) {
            data[0] = a.toByte()
            data[1] = b.toByte()
            data[2] = c.toByte()
            data[3] = d.toByte()
        }

        var a: Int
            get() = data[0].toInt()
            set(value) {
                when (value) {
                    in 10..100 -> {
                        data[0] = a.toByte()
                    }

                    in 101..600 -> {
                        data[0] = ((value - 100) / 5 + 100).toByte()
                    }

                    in 601..1000 -> {
                        data[0] = ((value - 600) / 10 + 200).toByte()
                    }

                    else -> {
                        data[0] = 10
                    }
                }
            }

        var b: Int
            get() = data[0].toInt()
            set(value) {
                when (value) {
                    in 10..100 -> {
                        data[1] = a.toByte()
                    }

                    in 101..600 -> {
                        data[1] = ((value - 100) / 5 + 100).toByte()
                    }

                    in 601..1000 -> {
                        data[1] = ((value - 600) / 10 + 200).toByte()
                    }

                    else -> {
                        data[1] = 10
                    }
                }
            }

        var c: Int
            get() = data[0].toInt()
            set(value) {
                when (value) {
                    in 10..100 -> {
                        data[2] = a.toByte()
                    }

                    in 101..600 -> {
                        data[2] = ((value - 100) / 5 + 100).toByte()
                    }

                    in 601..1000 -> {
                        data[2] = ((value - 600) / 10 + 200).toByte()
                    }

                    else -> {
                        data[2] = 10
                    }
                }
            }

        var d: Int
            get() = data[0].toInt()
            set(value) {
                when (value) {
                    in 10..100 -> {
                        data[3] = a.toByte()
                    }

                    in 101..600 -> {
                        data[3] = ((value - 100) / 5 + 100).toByte()
                    }

                    in 601..1000 -> {
                        data[3] = ((value - 600) / 10 + 200).toByte()
                    }

                    else -> {
                        data[3] = 10
                    }
                }
            }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Frequency

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class Strength(val data: ByteArray) {
        constructor(a: Int, b: Int, c: Int, d: Int) : this(data = ByteArray(4)) {
            data[0] = a.toByte()
            data[1] = b.toByte()
            data[2] = c.toByte()
            data[3] = d.toByte()
        }

        var a: Int
            get() = data[0].toInt()
            set(value)  {
                if (value < 0 || value > 100) throw DataOverflowException()
                data[0] = value.toByte()
            }

        var b: Int
            get() = data[0].toInt()
            set(value)  {
                if (value < 0 || value > 100) throw DataOverflowException()
                data[1] = value.toByte()
            }

        var c: Int
            get() = data[0].toInt()
            set(value)  {
                if (value < 0 || value > 100) throw DataOverflowException()
                data[2] = value.toByte()
            }

        var d: Int
            get() = data[0].toInt()
            set(value)  {
                if (value < 0 || value > 100) throw DataOverflowException()
                data[3] = value.toByte()
            }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Strength

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    constructor(frequency: Frequency, strength: Strength) : this(wave = ByteArray(8)) {
        for (i in 0 until 4) {
            wave[i] = frequency.data[i]
            wave[i + 4] = strength.data[i]
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as WaveData

        if (!wave.contentEquals(other.wave)) return false

        return true
    }

    override fun hashCode(): Int {
        return wave.contentHashCode()
    }

    companion object {
        fun convertFromV2(old: com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.WaveData): WaveData {
            val freConv = convertFrequency((old.x + old.y))
            val frequency = Frequency(freConv, freConv, freConv, freConv)
            val strengthValue = old.z * 5
            val strength = Strength(strengthValue, strengthValue, strengthValue, strengthValue)
            return WaveData(frequency, strength)
        }

        private fun convertFrequency(value: Int): Int {
            return when (value) {
                in 10..100 -> {
                   value
                }
                in 101..600 -> {
                    ((value - 100) / 5 + 100)
                }
                in 601..1000 -> {
                   ((value - 600) / 10 + 200)
                }
                else -> {
                    10
                }
            }
        }
    }
}