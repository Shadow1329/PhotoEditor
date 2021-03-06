package com.test.photoeditor.presentation.gallery

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.test.photoeditor.domain.interactor.ImageListGet
import com.test.photoeditor.domain.model.ImageItem
import io.reactivex.observers.DisposableSingleObserver

@InjectViewState
class GalleryPresenter(private val context: Context, private val imagesListGet: ImageListGet) : MvpPresenter<GalleryView>() {
    override fun onDestroy() {
        super.onDestroy()
        imagesListGet.dispose()
    }

    fun updateImageList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_PERMISSION_RESPONSE)
        } else {
            loadImages()
        }
    }

    private fun loadImages() {
        imagesListGet.execute(ImageListObserver(), null)
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        viewState.onRequestForPermissions(arrayOf(permission), requestCode)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            viewState.onCheckPermissionRationale(permission, requestCode)
        } else {
            loadImages()
        }
    }

    fun handlePermissionRationale(permission: String, requestCode: Int, result: Boolean) {
        if (!result) {
            viewState.onShowRationaleDialog(permission, requestCode, "You need to allow access to storage")
        } else {
            requestPermission(permission, requestCode)
        }
    }

    fun rationaleClosed(permission: String, requestCode: Int) {
        requestPermission(permission, requestCode)
    }

    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_PERMISSION_RESPONSE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImages()
                }
            }
        }
    }

    fun imageSelected(image: ImageItem) {
        viewState.onStartEditorActivity(image.path)
    }

    companion object {
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_RESPONSE = 4001
    }

    private inner class ImageListObserver: DisposableSingleObserver<List<ImageItem>>() {
        override fun onSuccess(t: List<ImageItem>) {
            viewState.onShowImages(t)
        }

        override fun onError(e: Throwable) {
            e.message?.let {
                viewState.onShowMessage(it)
            }
        }
    }
}