package com.example.sricedemo

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable

class StickerScatterDrawable(private val context: Context) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val stickers = listOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.person),
        BitmapFactory.decodeResource(context.resources, R.drawable.shovels),
        BitmapFactory.decodeResource(context.resources, R.drawable.weed)
    )

    private data class StickerPlacement(
        val bitmap: Bitmap,
        val x: Float,
        val y: Float,
        val scale: Float,
        val rotation: Float
    )

    private val placements = mutableListOf<StickerPlacement>()
    private var initialized = false

    override fun draw(canvas: Canvas) {
        if (!initialized) {
            initializePlacements()
        }

        for (placement in placements) {
            canvas.save()
            canvas.translate(placement.x, placement.y)
            canvas.rotate(placement.rotation)
            canvas.scale(placement.scale, placement.scale)
            canvas.drawBitmap(
                placement.bitmap,
                -placement.bitmap.width / 2f,
                -placement.bitmap.height / 2f,
                paint
            )
            canvas.restore()
        }
    }

    private fun initializePlacements() {
        val width = bounds.width()
        val height = bounds.height()

        // Custom placements (x%, y%, scale)
        val basePlacements = listOf(
            // Top Row
            Triple(0.05f, 0.05f, 0.15f),
            Triple(0.18f, 0.07f, 0.1f),
            Triple(0.33f, 0.05f, 0.2f),
            Triple(0.5f, 0.07f, 0.12f),
            Triple(0.67f, 0.06f, 0.22f),
            Triple(0.83f, 0.05f, 0.1f),
            Triple(0.95f, 0.08f, 0.18f),

            // Upper-mid
            Triple(0.12f, 0.22f, 0.25f),
            Triple(0.3f, 0.2f, 0.15f),
            Triple(0.47f, 0.25f, 0.22f),
            Triple(0.65f, 0.18f, 0.1f),
            Triple(0.85f, 0.22f, 0.2f),

            // Middle
            Triple(0.08f, 0.5f, 0.18f),
            Triple(0.2f, 0.45f, 0.12f),
            Triple(0.35f, 0.5f, 0.3f),
            Triple(0.52f, 0.48f, 0.15f),
            Triple(0.68f, 0.52f, 0.25f),
            Triple(0.82f, 0.47f, 0.2f),
            Triple(0.95f, 0.5f, 0.1f),

            // Lower-mid
            Triple(0.1f, 0.7f, 0.14f),
            Triple(0.28f, 0.72f, 0.2f),
            Triple(0.5f, 0.68f, 0.1f),
            Triple(0.7f, 0.73f, 0.2f),
            Triple(0.88f, 0.7f, 0.13f),

            // Bottom Row
            Triple(0.04f, 0.92f, 0.15f),
            Triple(0.2f, 0.95f, 0.1f),
            Triple(0.36f, 0.9f, 0.2f),
            Triple(0.5f, 0.93f, 0.12f),
            Triple(0.67f, 0.94f, 0.22f),
            Triple(0.82f, 0.91f, 0.1f),
            Triple(0.96f, 0.95f, 0.18f),
        )

        val rotations = listOf(-20f, -10f, -5f, 0f, 5f, 10f, 15f, 20f)

        for ((index, triple) in basePlacements.withIndex()) {
            val (nx, ny, scale) = triple
            val x = nx * width
            val y = ny * height
            val bitmap = stickers[index % stickers.size]
            val rotation = rotations[index % rotations.size]

            placements.add(StickerPlacement(bitmap, x, y, scale, rotation))
        }

        initialized = true
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}
