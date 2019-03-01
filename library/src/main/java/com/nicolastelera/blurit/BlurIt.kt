package com.nicolastelera.blurit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.renderscript.*
import android.support.annotation.RequiresApi

@RequiresApi(value = Build.VERSION_CODES.JELLY_BEAN_MR1)
class BlurIt(context: Context) {

    private val renderScript: RenderScript = RenderScript.create(context)
    private val script: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

    @RequiresApi(value = Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun blurBitmap(bitmap: Bitmap, intensity: Float): Bitmap = blur(bitmap, intensity)

    @RequiresApi(value = Build.VERSION_CODES.LOLLIPOP)
    fun blurBitmapPart(bitmap: Bitmap, intensity: Float, bounds: Rect): Bitmap {
        val launchOptions = with(bounds) {
            Script.LaunchOptions().apply {
                setX(left, right)
                setY(top, bottom)
            }
        }
        return blur(bitmap, intensity, true, launchOptions)
    }

    fun destroy() {
        script.destroy()
        renderScript.destroy()
    }

    private fun blur(
        bitmap: Bitmap,
        intensity: Float,
        shouldBlurPart: Boolean = false,
        launchOptions: Script.LaunchOptions? = null
    ): Bitmap {
        val output: Bitmap = bitmap.copy(bitmap.config, true)
        val inAllocation: Allocation = Allocation.createFromBitmap(
            renderScript,
            bitmap,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_GRAPHICS_TEXTURE
        )
        val outAllocation: Allocation = Allocation.createFromBitmap(renderScript, output)

        script.apply {
            setRadius(intensity)
            setInput(inAllocation)
            if (shouldBlurPart) forEach(outAllocation, launchOptions) else forEach(outAllocation)
        }

        outAllocation.copyTo(output)
        inAllocation.destroy()
        outAllocation.destroy()

        return output
    }
}
