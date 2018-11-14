package com.test.photoeditor.presentation.gallery

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.test.photoeditor.R
import com.test.photoeditor.domain.model.ImageItem
import com.test.photoeditor.internal.PhotoEditorApplication
import com.test.photoeditor.presentation.editor.EditorActivity
import kotlinx.android.synthetic.main.activity_gallery.*
import javax.inject.Inject

class GalleryActivity : MvpAppCompatActivity(), GalleryView, ImageAdapterListener {
    @Inject
    @InjectPresenter
    lateinit var galleryPresenter: GalleryPresenter

    @ProvidePresenter
    fun providePresenter(): GalleryPresenter {
        return galleryPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PhotoEditorApplication).getApplicationComponent().inject(this)
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setContentView(R.layout.activity_gallery)
        imageGalleryView.setHasFixedSize(true)
        imageGalleryView.layoutManager = GridLayoutManager(this, 4)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        galleryPresenter.handlePermissionResult(requestCode, grantResults)
    }

    override fun onImageSelected(image: ImageItem) {
        galleryPresenter.imageSelected(image)
    }

    override fun onShowImages(images: List<ImageItem>) {
        val imagesAdapter = ImageAdapter(images, this)
        imageGalleryView.adapter = imagesAdapter
    }

    override fun onStartEditorActivity(imagePath: String) {
        EditorActivity.start(this, imagePath)
    }

    override fun onCheckPermissionRationale(permission: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            galleryPresenter.handlePermissionRationale(permission, requestCode, shouldShowRequestPermissionRationale(permission))
        }
    }

    override fun onShowRationaleDialog(permission: String, requestCode: Int, text: String) {
        AlertDialog.Builder(this)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                    galleryPresenter.rationaleClosed(permission, requestCode)
                }
                .create()
                .show()
    }

    override fun onRequestForPermissions(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onShowMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
