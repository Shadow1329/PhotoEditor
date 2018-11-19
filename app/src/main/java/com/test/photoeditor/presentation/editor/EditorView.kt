package com.test.photoeditor.presentation.editor

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.test.photoeditor.domain.model.HSLFilter

@StateStrategyType(AddToEndSingleStrategy::class)
interface EditorView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeActivity()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSaveDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setHSLBars(hslFilter: HSLFilter)

    fun setRedFilter(hslFilter: HSLFilter)
    fun setYellowFilter(hslFilter: HSLFilter)
    fun setGreenFilter(hslFilter: HSLFilter)
    fun setAquaFilter(hslFilter: HSLFilter)
    fun setBlueFilter(hslFilter: HSLFilter)
    fun setPurpleFilter(hslFilter: HSLFilter)
    fun updateImage()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onShowMessage(message: String)
}