package com.nicolastelera.bluritsample

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nicolastelera.blurit.BlurIt
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var blurIt: BlurIt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        blurIt = BlurIt(this)
        initView()
    }

    private fun initView() {
        val srcBitmap = BitmapFactory.decodeResource(resources, R.drawable.landscape)
        blurImageView.setImageBitmap(srcBitmap)
        blurButton.setOnClickListener {
            val blurredSrc = blurIt.blurBitmap(srcBitmap, 25f)
            blurImageView.setImageBitmap(blurredSrc)
        }
        blurPartButton.setOnClickListener {
            val bounds = Rect(
                srcBitmap.width / 4,
                srcBitmap.height / 4,
                (srcBitmap.width * 0.75f).toInt(),
                (srcBitmap.height * 0.75).toInt()
            )
            val blurredSrc = blurIt.blurBitmapPart(srcBitmap, 25f, bounds)
            blurImageView.setImageBitmap(blurredSrc)
        }
    }

    override fun onDestroy() {
        blurIt.destroy()
        super.onDestroy()
    }
}
