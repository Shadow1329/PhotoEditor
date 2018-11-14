package com.test.photoeditor.presentation.editor

import android.content.Context
import android.os.Bundle
import com.test.photoeditor.R
import android.content.Intent
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.squareup.picasso.Picasso
import com.test.photoeditor.internal.PhotoEditorApplication
import kotlinx.android.synthetic.main.activity_editor.*
import javax.inject.Inject

class EditorActivity : MvpAppCompatActivity(), EditorView {
    @Inject
    @InjectPresenter
    lateinit var editorPresenter: EditorPresenter

    @ProvidePresenter
    fun providePresenter(): EditorPresenter {
        return editorPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PhotoEditorApplication).getApplicationComponent().inject(this)
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setContentView(R.layout.activity_editor)

        val imagePath = intent.getStringExtra(IMAGE_PATH)
        Picasso.get()
                .load("file://$imagePath")
                .placeholder(R.color.colorImagePlaceHolder)
                .into(imageView)
    }

    companion object {
        const val IMAGE_PATH = "image_path"

        fun start(context: Context, imagePath: String) {
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra(IMAGE_PATH, imagePath)
            context.startActivity(intent)
        }
    }
}
