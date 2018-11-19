package com.test.photoeditor.presentation.editor

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.test.photoeditor.R
import android.content.Intent
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.test.photoeditor.internal.PhotoEditorApplication
import kotlinx.android.synthetic.main.activity_editor.*
import javax.inject.Inject
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.RenderScript
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import com.test.photoeditor.ScriptC_filter
import com.test.photoeditor.domain.model.HSLFilter

class EditorActivity : MvpAppCompatActivity(), EditorView {
    @Inject
    @InjectPresenter
    lateinit var editorPresenter: EditorPresenter

    private lateinit var filterScript: ScriptC_filter
    private lateinit var allocationIn: Allocation
    private lateinit var allocationOut: Allocation
    private lateinit var outputBitmap: Bitmap

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

        val inputBitmap = loadBitmap(imagePath)
        outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(this)
        filterScript = ScriptC_filter(rs)
        allocationIn = Allocation.createFromBitmap(rs, inputBitmap)
        allocationOut = Allocation.createFromBitmap(rs, outputBitmap)

        imageView.setImageBitmap(outputBitmap)

        hueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    editorPresenter.hueBarChanged(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        saturationBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    editorPresenter.saturationBarChanged(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        lightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    editorPresenter.lightnessBarChanged(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        colorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            editorPresenter.colorSelectChanged(checkedId)
        }

        closeButton.setOnClickListener { editorPresenter.closeButtonPressed() }

        saveButton.setOnClickListener { editorPresenter.saveButtonPressed() }
    }

    private fun loadBitmap(path: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(path, options)
    }

    override fun closeActivity() {
        finish()
    }

    override fun showSaveDialog() {
        val alert = AlertDialog.Builder(this)

        alert.setTitle("Save image")
        alert.setMessage("Enter filename")

        val fileName = EditText(this)
        alert.setView(fileName)

        alert.setPositiveButton("Ok") { _, _ ->
            editorPresenter.saveImage(fileName.text.toString(), outputBitmap)
        }

        alert.setNegativeButton("Cancel", null)
        alert.show()
    }

    override fun setHSLBars(hslFilter: HSLFilter) {
        hueBar.progress = hslFilter.hueRaw
        saturationBar.progress = hslFilter.saturationRaw
        lightnessBar.progress = hslFilter.lightnessRaw
    }

    override fun setRedFilter(hslFilter: HSLFilter) {
        filterScript.invoke_setRedFilter(hslFilter.hue, hslFilter.saturation, hslFilter.lightness)
    }

    override fun setYellowFilter(hslFilter: HSLFilter) {
        filterScript.invoke_setYellowFilter(hslFilter.hue, hslFilter.saturation, hslFilter.lightness)
    }

    override fun setGreenFilter(hslFilter: HSLFilter) {
        filterScript.invoke_setGreenFilter(hslFilter.hue, hslFilter.saturation, hslFilter.lightness)
    }

    override fun setAquaFilter(hslFilter: HSLFilter) {
        filterScript.invoke_setAquaFilter(hslFilter.hue, hslFilter.saturation, hslFilter.lightness)
    }

    override fun setBlueFilter(hslFilter: HSLFilter) {
        filterScript.invoke_setBlueFilter(hslFilter.hue, hslFilter.saturation, hslFilter.lightness)
    }

    override fun setPurpleFilter(hslFilter: HSLFilter) {
        filterScript.invoke_setPurpleFilter(hslFilter.hue, hslFilter.saturation, hslFilter.lightness)
    }

    override fun updateImage() {
        filterScript.forEach_filter(allocationIn, allocationOut)
        allocationOut.copyTo(outputBitmap)
    }

    override fun onShowMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val IMAGE_PATH = "image_path"

        fun start(context: Context, imagePath: String) {
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra(IMAGE_PATH, imagePath)
            context.startActivity(intent)
        }
    }
}
