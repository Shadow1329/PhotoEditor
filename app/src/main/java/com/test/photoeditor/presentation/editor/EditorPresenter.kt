package com.test.photoeditor.presentation.editor

import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.test.photoeditor.R
import com.test.photoeditor.domain.interactor.ImageSave
import com.test.photoeditor.domain.model.ColorType
import com.test.photoeditor.domain.model.HSLFilter
import io.reactivex.observers.DisposableCompletableObserver
import java.io.ByteArrayOutputStream

@InjectViewState
class EditorPresenter(private val imageSave: ImageSave) : MvpPresenter<EditorView>() {
    private val filters = Array(ColorType.values().size) { HSLFilter(DIVIDER, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE) }
    private lateinit var selectedColor: ColorType

    init {
        for (color in ColorType.values()) {
            updateFilter(color, filters[color.ordinal])
        }
        selectColor(ColorType.RED)
        viewState.updateImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        imageSave.dispose()
    }

    fun closeButtonPressed() {
        viewState.closeActivity()
    }

    fun saveButtonPressed() {
        viewState.showSaveDialog()
    }

    fun saveImage(fileName: String, bitmap: Bitmap) {
        if (!fileName.isEmpty()) {
            val byteStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
            imageSave.execute(ImageSaveObserver(), Pair(fileName, byteStream))
        } else {
            viewState.onShowMessage("Filename is empty")
        }
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

    private inner class ImageSaveObserver: DisposableCompletableObserver() {
        override fun onComplete() {
            viewState.onShowMessage("Image saved")
        }

        override fun onError(e: Throwable) {
            e.message?.let {
                viewState.onShowMessage(it)
            }
        }
    }
}