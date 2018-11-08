package com.test.photoeditor.presentation.gallery

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.test.photoeditor.domain.model.ImageItem

@InjectViewState
class GalleryPresenter : MvpPresenter<GalleryView>() {
    fun onLoadImages() {
        val imageList = mutableListOf<ImageItem>()
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        imageList.add(ImageItem("aa", "aa"))
        viewState.onShowImages(imageList)
    }
}