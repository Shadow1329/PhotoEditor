package com.test.photoeditor.presentation.editor

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.test.photoeditor.R
import com.test.photoeditor.domain.model.ColorType
import com.test.photoeditor.domain.model.HSLFilter

@InjectViewState
class EditorPresenter : MvpPresenter<EditorView>() {
    private val filters = Array(ColorType.values().size) { HSLFilter(DIVIDER, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE) }
    private lateinit var selectedColor: ColorType

    init {
        for (color in ColorType.values()) {
            updateFilter(color, filters[color.ordinal])
        }
        selectColor(ColorType.RED)
        viewState.updateImage()
    }

    fun closeButtonPressed() {
        viewState.closeActivity()
    }

    fun saveButtonPressed() {

    }

    fun hueBarChanged(value: Int) {
        filters[selectedColor.ordinal].hueRaw = value
        updateFilter(selectedColor, filters[selectedColor.ordinal])
        viewState.updateImage()
    }

    fun saturationBarChanged(value: Int) {
        filters[selectedColor.ordinal].saturationRaw = value
        updateFilter(selectedColor, filters[selectedColor.ordinal])
        viewState.updateImage()
    }

    fun lightnessBarChanged(value: Int) {
        filters[selectedColor.ordinal].lightnessRaw = value
        updateFilter(selectedColor, filters[selectedColor.ordinal])
        viewState.updateImage()
    }

    fun colorSelectChanged(selectedId: Int) {
        when (selectedId) {
            R.id.redButton -> selectColor(ColorType.RED)
            R.id.yellowButton -> selectColor(ColorType.YELLOW)
            R.id.greenButton -> selectColor(ColorType.GREEN)
            R.id.aquaButton -> selectColor(ColorType.AQUA)
            R.id.blueButton -> selectColor(ColorType.BLUE)
            R.id.purpleButton -> selectColor(ColorType.PURPLE)
        }
    }

    private fun updateFilter(color: ColorType, filter: HSLFilter) {
        when (color) {
            ColorType.RED -> viewState.setRedFilter(filter)
            ColorType.YELLOW -> viewState.setYellowFilter(filter)
            ColorType.GREEN -> viewState.setGreenFilter(filter)
            ColorType.AQUA -> viewState.setAquaFilter(filter)
            ColorType.BLUE -> viewState.setBlueFilter(filter)
            ColorType.PURPLE -> viewState.setPurpleFilter(filter)
        }
    }

    private fun selectColor(color: ColorType) {
        selectedColor = color
        viewState.setHSLBars(filters[color.ordinal])
    }

    companion object {
        private const val DEFAULT_VALUE = 50
        private const val DIVIDER = 100.0f
    }
}