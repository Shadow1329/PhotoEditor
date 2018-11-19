package com.test.photoeditor.domain.model

class HSLFilter(var divider: Float, var hueRaw: Int, var saturationRaw: Int, var lightnessRaw: Int) {
    val hue: Float
        get() = hueRaw.div(divider)

    val saturation: Float
        get() = saturationRaw.div(divider)

    val lightness: Float
        get() = lightnessRaw.div(divider)
}