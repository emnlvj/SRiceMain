package com.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sricedemo.R

class SlideAdapterWelcome(private val items: List<SlideItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_WELCOME = 0
        const val TYPE_FEATURE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_WELCOME else TYPE_FEATURE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_WELCOME) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_welcome, parent, false)
            WelcomeViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_feature, parent, false)
            FeatureViewHolder(view)
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is WelcomeViewHolder) {
            holder.bind(item)
        } else if (holder is FeatureViewHolder) {
            holder.bind(item)
        }
    }

    class WelcomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SlideItem) {
            val title = itemView.findViewById<TextView>(R.id.titleWelcome)
            val message = itemView.findViewById<TextView>(R.id.messageWelcome)
            title.text = item.title
            message.text = item.description
        }
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SlideItem) {
            val title = itemView.findViewById<TextView>(R.id.titleFeature)
            val desc = itemView.findViewById<TextView>(R.id.descriptionFeature)
            title.text = item.title
            desc.text = item.description
        }
    }
}
