package com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data

data class TouchWaveData(val ax: Int, val ay: Int, val az: Int) {
    enum class TouchWaveType(val data: Array<TouchWaveData>) {
        EXTRUSION(arrayOf(
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20)
        )),
        BUBBLE(arrayOf(
            TouchWaveData(3, 42, 0),
            TouchWaveData(3, 42, 20)
        )),
        SIGNAL(arrayOf(
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 10),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 10),
            TouchWaveData(1, 9, 20),
            TouchWaveData(3, 42, 0),
            TouchWaveData(2, 23, 20),
            TouchWaveData(2, 28, 20),
            TouchWaveData(3, 35, 20),
            TouchWaveData(3, 43, 20),
            TouchWaveData(3, 54, 20),
            TouchWaveData(0, 0, 0),
            TouchWaveData(0, 0, 0)
        )),
        CLIMB(arrayOf(
            TouchWaveData(3, 53, 10),
            TouchWaveData(3, 36, 12),
            TouchWaveData(2, 26, 14),
            TouchWaveData(2, 17, 16),
            TouchWaveData(1, 13, 18),
            TouchWaveData(1, 9, 20)
        )),
        RHYTHM(arrayOf(
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 20)
        )),
        KEEP_CLICK(arrayOf(
            TouchWaveData(5, 95, 20)
        )),
        PULSE(arrayOf(
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 11, 20),
            TouchWaveData(1, 14, 20),
            TouchWaveData(2, 16, 20),
            TouchWaveData(2, 20, 20),
            TouchWaveData(2, 26, 20),
            TouchWaveData(2, 32, 20),
            TouchWaveData(3, 39, 20),
            TouchWaveData(3, 49, 20),
            TouchWaveData(4, 60, 20),
            TouchWaveData(4, 75, 20),
            TouchWaveData(4, 93, 20),
            TouchWaveData(5, 115, 20),
            TouchWaveData(6, 141, 20),
            TouchWaveData(6, 175, 20),
            TouchWaveData(7, 216, 20)
        )),
        AIR_WAVES(arrayOf(
            TouchWaveData(1, 9, 20),
            TouchWaveData(2, 15, 20),
            TouchWaveData(2, 28, 20),
            TouchWaveData(3, 49, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(1, 9, 0),
            TouchWaveData(1, 9, 20),
            TouchWaveData(0, 0, 0),
            TouchWaveData(0, 0, 0)
        ));
    }

}