package com.nicolastelera.bluritsample

import android.graphics.Bitmap
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

        with(BitmapFactory.decodeResource(resources, R.drawable.landscape)) {
            blurImageView.setImageBitmap(this)
            initClickListener(this)
        }
    }

    override fun onDestroy() {
        blurIt.destroy()
        super.onDestroy()
    }

    private fun initClickListener(srcBitmap: Bitmap) {
        blurButton.setOnClickListener {
            val blurredSrc = blurIt.blurBitmap(srcBitmap, 25f)
            blurImageView.setImageBitmap(blurredSrc)
        }

        blurPartButton.setOnClickListener {
            val bounds = with(srcBitmap) {
                Rect(
                    width.percent(0.25f),
                    height.percent(0.25f),
                    width.percent(0.75f),
                    height.percent(0.75f)
                )
            }
            val blurredSrc = blurIt.blurBitmapPart(srcBitmap, 25f, bounds)
            blurImageView.setImageBitmap(blurredSrc)
        }
    }
}

private fun Int.percent(percent: Float) = (this * percent).toInt()
