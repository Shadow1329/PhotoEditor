package com.test.photoeditor.presentation.editor

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
import android.widget.SeekBar
import com.test.photoeditor.ScriptC_filter

class EditorActivity : MvpAppCompatActivity(), EditorView {
    enum class ColorType {
        RED, YELLOW, GREEN, AQUA, BLUE, PURPLE
    }

    @Inject
    @InjectPresenter
    lateinit var editorPresenter: EditorPresenter

    private lateinit var filterScript: ScriptC_filter
    private lateinit var allocationIn: Allocation
    private lateinit var allocationOut: Allocation
    private lateinit var outputBitmap: Bitmap
    private lateinit var selectedColor: ColorType

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

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        val inputBitmap = BitmapFactory.decodeFile(imagePath, options)
        outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(this)
        filterScript = ScriptC_filter(rs)
        allocationIn = Allocation.createFromBitmap(rs, inputBitmap)
        allocationOut = Allocation.createFromBitmap(rs, outputBitmap)
        imageView.setImageBitmap(outputBitmap)

        hueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setHue(selectedColor, i.div(100f))
                filter()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        saturationBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setSaturation(selectedColor, i.div(100f))
                filter()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        lightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setLightness(selectedColor, i.div(100f))
                filter()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        colorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.redButton -> selectColor(ColorType.RED)
                R.id.yellowButton -> selectColor(ColorType.YELLOW)
                R.id.greenButton -> selectColor(ColorType.GREEN)
                R.id.aquaButton -> selectColor(ColorType.AQUA)
                R.id.blueButton -> selectColor(ColorType.BLUE)
                R.id.purpleButton -> selectColor(ColorType.PURPLE)
            }
        }

        colorRadioGroup.check(R.id.blueButton)
        setHue(ColorType.RED, 0.5f)
        setHue(ColorType.YELLOW, 0.5f)
        setHue(ColorType.GREEN, 0.5f)
        setHue(ColorType.AQUA, 0.5f)
        setHue(ColorType.BLUE, 0.5f)
        setHue(ColorType.PURPLE, 0.5f)
    }

    private fun selectColor(color: ColorType) {
        selectedColor = color
    }

    private fun setHue(color: ColorType, value: Float) {
        when(color) {
            ColorType.RED -> filterScript.invoke_setRedFilter(value, 0.5f, 0.5f)
            ColorType.YELLOW -> filterScript.invoke_setYellowFilter(value, 0.5f, 0.5f)
            ColorType.GREEN -> filterScript.invoke_setGreenFilter(value, 0.5f, 0.5f)
            ColorType.AQUA -> filterScript.invoke_setAquaFilter(value, 0.5f, 0.5f)
            ColorType.BLUE -> filterScript.invoke_setBlueFilter(value, 0.5f, 0.5f)
            ColorType.PURPLE -> filterScript.invoke_setPurpleFilter(value, 0.5f, 0.5f)
        }
    }

    private fun setSaturation(color: ColorType, value: Float) {
        when(color) {
            ColorType.RED -> filterScript.invoke_setRedFilter(0.5f, value, 0.5f)
        }
    }

    private fun setLightness(color: ColorType, value: Float) {
        when(color) {
            ColorType.RED -> filterScript.invoke_setRedFilter(0.5f, 0.5f, value)
        }
    }

    private fun filter() {
        filterScript.forEach_filter(allocationIn, allocationOut)
        allocationOut.copyTo(outputBitmap)
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
