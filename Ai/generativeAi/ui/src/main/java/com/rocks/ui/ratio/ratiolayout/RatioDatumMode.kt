package com.rocks.ui.ratio.ratiolayout

enum class RatioDatumMode(val mode: Int) {
    DATUM_AUTO(0), DATUM_WIDTH(1), DATUM_HEIGHT(2);

    companion object {
        fun valueOf(mode: Int): RatioDatumMode {
            if (mode == DATUM_WIDTH.mode) {
                return DATUM_WIDTH
            }
            return if (mode == DATUM_HEIGHT.mode) {
                DATUM_HEIGHT
            } else DATUM_AUTO
        }
    }
}