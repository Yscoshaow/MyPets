package com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data

import kotlin.math.ceil

// l - 休息时长 小节间休息时长 向上取整(L/10)
// zy - 高低频平衡 取值 0-20 越低高频越强 越高低频更强
data class AutoWaveData(val sections: Array<AutoWaveSection>, val l: Int, val zy: Int) {
    val waveMaxTimingSection: Array<Int> =
        this.sections.map { ceil(it.j.toDouble() / it.c.toDouble()).toInt() }.toTypedArray()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AutoWaveData

        if (!sections.contentEquals(other.sections)) return false
        if (l != other.l) return false
        if (zy != other.zy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sections.contentHashCode()
        result = 31 * result + l
        result = 31 * result + zy
        return result
    }
    enum class AutoWaveType(val waveName: String, val data: AutoWaveData) {
        OFF(
            "关闭",
            AutoWaveData(
                sections = arrayOf(
                    AutoWaveSection(0, 0, 0, 0, 0, arrayOf(
                        AutoWavePoint(0, 0.0, 1), AutoWavePoint(0, 0.0, 1)
                    ))
                ),
                l = 0, zy = 0
            )
        ),
        BREATH(
            "呼吸",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 20, 8, 0, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 4.0, 0),
                    AutoWavePoint(2, 8.0, 0),
                    AutoWavePoint(3, 12.0, 0),
                    AutoWavePoint(4, 16.0, 0),
                    AutoWavePoint(5, 20.0, 1),
                    AutoWavePoint(6, 20.0, 1),
                    AutoWavePoint(7, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=35, zy=8)
        ),
        TIDE(
            "潮汐",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 32, 11, 20, 2, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 3.3333333, 0),
                    AutoWavePoint(2, 6.6666665, 0),
                    AutoWavePoint(3, 10.0, 1),
                    AutoWavePoint(4, 13.333333, 0),
                    AutoWavePoint(5, 16.666666, 0),
                    AutoWavePoint(6, 20.0, 1),
                    AutoWavePoint(7, 18.402824, 0),
                    AutoWavePoint(8, 16.805649, 0),
                    AutoWavePoint(9, 15.208473, 0),
                    AutoWavePoint(10, 13.611298, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=5, zy=8)
        ),
        KEEP_CLICK(
            "连击",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 34, 8, 20, 1, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 0.0, 1),
                    AutoWavePoint(2, 20.0, 1),
                    AutoWavePoint(3, 13.333333, 0),
                    AutoWavePoint(4, 6.6666665, 0),
                    AutoWavePoint(5, 0.0, 1),
                    AutoWavePoint(6, 0.2399439, 0),
                    AutoWavePoint(7, 0.4798878, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=0, zy=8)
        ),
        QUICK_PRESS(
            "快速按捏",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 29, 2, 44, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=16, zy=8)
        ),
        STRONGER(
            "按捏渐强",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 20, 11, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 5.719211, 1),
                    AutoWavePoint(2, 0.0, 1),
                    AutoWavePoint(3, 10.499579, 1),
                    AutoWavePoint(4, 0.3826058, 1),
                    AutoWavePoint(5, 14.682558, 1),
                    AutoWavePoint(6, 0.0, 1),
                    AutoWavePoint(7, 17.454998, 1),
                    AutoWavePoint(8, 0.0, 1),
                    AutoWavePoint(9, 20.0, 1),
                    AutoWavePoint(10, 0.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=5, zy=8)
        ),
        HEARTBEAT(
            "心跳节奏",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(65, 20, 2, 6, 1, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 14, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 0.0, 0),
                    AutoWavePoint(2, 0.0, 0),
                    AutoWavePoint(3, 0.0, 0),
                    AutoWavePoint(4, 0.0, 1),
                    AutoWavePoint(5, 14.972563, 1),
                    AutoWavePoint(6, 16.648375, 0),
                    AutoWavePoint(7, 18.324188, 0),
                    AutoWavePoint(8, 20.0, 1),
                    AutoWavePoint(9, 0.0, 1),
                    AutoWavePoint(10, 0.0, 0),
                    AutoWavePoint(11, 0.0, 0),
                    AutoWavePoint(12, 0.0, 0),
                    AutoWavePoint(13, 0.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=5, zy=16)
        ),
        COMPRESS(
            "压缩",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(52, 16, 11, 0, 2, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 0),
                    AutoWavePoint(2, 20.0, 0),
                    AutoWavePoint(3, 20.0, 0),
                    AutoWavePoint(4, 20.0, 0),
                    AutoWavePoint(5, 20.0, 0),
                    AutoWavePoint(6, 20.0, 0),
                    AutoWavePoint(7, 20.0, 0),
                    AutoWavePoint(8, 20.0, 0),
                    AutoWavePoint(9, 20.0, 0),
                    AutoWavePoint(10, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 10, 0, 1, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 0),
                    AutoWavePoint(2, 20.0, 0),
                    AutoWavePoint(3, 20.0, 0),
                    AutoWavePoint(4, 20.0, 0),
                    AutoWavePoint(5, 20.0, 0),
                    AutoWavePoint(6, 20.0, 0),
                    AutoWavePoint(7, 20.0, 0),
                    AutoWavePoint(8, 20.0, 0),
                    AutoWavePoint(9, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=0, zy=16)
        ),
        RHYTHMIC(
            "节奏步伐",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 20, 26, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 4.0, 0),
                    AutoWavePoint(2, 8.0, 0),
                    AutoWavePoint(3, 12.0, 0),
                    AutoWavePoint(4, 16.0, 0),
                    AutoWavePoint(5, 20.0, 1),
                    AutoWavePoint(6, 0.0, 1),
                    AutoWavePoint(7, 5.0, 0),
                    AutoWavePoint(8, 10.0, 0),
                    AutoWavePoint(9, 15.0, 0),
                    AutoWavePoint(10, 20.0, 1),
                    AutoWavePoint(11, 0.0, 1),
                    AutoWavePoint(12, 6.6666665, 0),
                    AutoWavePoint(13, 13.333333, 0),
                    AutoWavePoint(14, 20.0, 1),
                    AutoWavePoint(15, 0.0, 1),
                    AutoWavePoint(16, 10.0, 0),
                    AutoWavePoint(17, 20.0, 1),
                    AutoWavePoint(18, 0.0, 1),
                    AutoWavePoint(19, 20.0, 1),
                    AutoWavePoint(20, 0.0, 1),
                    AutoWavePoint(21, 20.0, 1),
                    AutoWavePoint(22, 0.0, 1),
                    AutoWavePoint(23, 20.0, 1),
                    AutoWavePoint(24, 0.0, 1),
                    AutoWavePoint(25, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=5, zy=8)
        ),
        GRAIN_TOUCH(
            "颗粒摩擦",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 38, 4, 25, 2, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 0),
                    AutoWavePoint(2, 20.0, 1),
                    AutoWavePoint(3, 0.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=0, zy=8)
        ),
        SPRING(
            "渐变弹跳",
            AutoWaveData(
                sections=arrayOf(
                    AutoWaveSection(0, 30, 4, 45, 2, arrayOf(
                        AutoWavePoint(0, 0.18084228, 1),
                        AutoWavePoint(1, 6.7872286, 0),
                        AutoWavePoint(2, 13.393615, 0),
                        AutoWavePoint(3, 20.0, 1)
                    )),
                    AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                        AutoWavePoint(0, 0.0, 1),
                        AutoWavePoint(1, 20.0, 1)
                    )),
                    AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                        AutoWavePoint(0, 0.0, 1),
                        AutoWavePoint(1, 20.0, 1)
                    ))
                ), l=20, zy=16)
        ),
        WAVE_RIPPLE(
            "波浪涟漪",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 60, 4, 53, 4, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 10.0, 0),
                    AutoWavePoint(2, 20.0, 1),
                    AutoWavePoint(3, 14.669602, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=5, zy=16)
        ),
        RAIN_SWEPT(
            "雨水冲刷",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(4, 0, 3, 39, 1, arrayOf(
                    AutoWavePoint(0, 6.7057176, 1),
                    AutoWavePoint(1, 13.3528595, 0),
                    AutoWavePoint(2, 20.0, 1)
                )),
                AutoWaveSection(44, 54, 2, 35, 1, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=25, zy=8)
        ),
        KNOCK(
            "变速敲击",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(14, 20, 7, 41, 1, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 0),
                    AutoWavePoint(2, 20.0, 1),
                    AutoWavePoint(3, 0.0, 1),
                    AutoWavePoint(4, 0.0, 0),
                    AutoWavePoint(5, 0.0, 0),
                    AutoWavePoint(6, 0.0, 1)
                )),
                AutoWaveSection(65, 20, 4, 40, 1, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 0),
                    AutoWavePoint(2, 20.0, 0),
                    AutoWavePoint(3, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=15, zy=8)
        ),
        SIGNAL(
            "信号灯",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(78, 64, 4, 20, 1, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 20.0, 0),
                    AutoWavePoint(2, 20.0, 0),
                    AutoWavePoint(3, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 4, 20, 3, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 6.6666665, 0),
                    AutoWavePoint(2, 13.333333, 0),
                    AutoWavePoint(3, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=0, zy=8)
        ),
        FLIRT1(
            "挑逗1",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(0, 20, 10, 36, 3, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 5.0, 0),
                    AutoWavePoint(2, 10.0, 0),
                    AutoWavePoint(3, 15.0, 0),
                    AutoWavePoint(4, 20.0, 1),
                    AutoWavePoint(5, 20.0, 1),
                    AutoWavePoint(6, 20.0, 1),
                    AutoWavePoint(7, 0.0, 1),
                    AutoWavePoint(8, 0.0, 0),
                    AutoWavePoint(9, 0.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 22, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=5, zy=8)
        ),
        FLIRT2(
            "挑逗2",
            AutoWaveData(sections=arrayOf(
                AutoWaveSection(27, 7, 10, 33, 3, arrayOf(
                    AutoWavePoint(0, 0.2853297, 1),
                    AutoWavePoint(1, 2.4758487, 0),
                    AutoWavePoint(2, 4.6663675, 0),
                    AutoWavePoint(3, 6.8568864, 0),
                    AutoWavePoint(4, 9.047405, 0),
                    AutoWavePoint(5, 11.237925, 0),
                    AutoWavePoint(6, 13.428443, 0),
                    AutoWavePoint(7, 15.618962, 0),
                    AutoWavePoint(8, 17.80948, 0),
                    AutoWavePoint(9, 20.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 40, 2, arrayOf(
                    AutoWavePoint(0, 20.0, 1),
                    AutoWavePoint(1, 0.0, 1)
                )),
                AutoWaveSection(0, 20, 2, 20, 1, arrayOf(
                    AutoWavePoint(0, 0.0, 1),
                    AutoWavePoint(1, 20.0, 1)
                ))
            ), l=18, zy=8)
        );
    }


}