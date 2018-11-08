package com.test.photoeditor.presentation.gallery

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.test.photoeditor.R
import com.test.photoeditor.domain.model.ImageItem
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : MvpAppCompatActivity(), GalleryView {
    @InjectPresenter
    lateinit var galleryPresenter: GalleryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        galleryPresenter.onLoadImages()
    }

    private fun setupUI() {
        setContentView(R.layout.activity_gallery)
        imageGalleryView.setHasFixedSize(true)
        imageGalleryView.layoutManager = GridLayoutManager(this, 4)
    }

    override fun onShowImages(images: List<ImageItem>) {
        val imagesAdapter = ImagesAdapter(images)
        imageGalleryView.adapter = imagesAdapter
        imageGalleryView.scheduleLayoutAnimation()
    }
}
