package com.example

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import android.content.Context
import com.example.sricedemo.R

class SlideAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = 2

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(context).inflate(R.layout.slide_video, parent, false)
            VideoViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.slide_image1, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoViewHolder) {
            // Pass the videoUri to bindVideo()
            holder.bindVideo(Uri.parse("android.resource://${holder.itemView.context.packageName}/raw/background"))
        } else if (holder is ImageViewHolder) {
            holder.imageView.setImageResource(R.drawable.image1) // Replace with your image
        }
    }

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val playerView: PlayerView = view.findViewById(R.id.playerView)
        private var exoPlayer: ExoPlayer? = null

        // Move the bindVideo method here
        fun bindVideo(videoUri: Uri) {
            exoPlayer = ExoPlayer.Builder(itemView.context).build()
            playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(videoUri)

            exoPlayer?.apply {
                setMediaItem(mediaItem)
                playWhenReady = true
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                prepare()
            }
        }

        fun releasePlayer() {
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewSlide)
    }
}

