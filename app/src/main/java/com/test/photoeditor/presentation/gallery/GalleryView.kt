package com.test.photoeditor.presentation.gallery

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.test.photoeditor.domain.model.ImageItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface GalleryView : MvpView {
    fun onShowImages(images: List<ImageItem>)
}