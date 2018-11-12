package com.test.photoeditor.presentation.gallery

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.test.photoeditor.domain.model.ImageItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface GalleryView : MvpView {
    fun onShowImages(images: List<ImageItem>)
    fun onCheckPermissionRationale(permission: String, requestCode: Int)
    fun onShowRationaleDialog(permission: String, requestCode: Int, text: String)
    fun onRequestForPermissions(permissions: Array<String>, requestCode: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onShowMessage(message: String)
}