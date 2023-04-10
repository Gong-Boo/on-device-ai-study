package com.parkjin.tensorflow_lite

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MainView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attributeSet, defStyle) {

    private val diffUtil = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) =
            oldItem.drawableResId == newItem.drawableResId

        override fun areContentsTheSame(oldItem: Item, newItem: Item) =
            oldItem.drawableResId == newItem.drawableResId
    }

    private val adapter = object : ListAdapter<Item, FlowerViewHolder>(diffUtil) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
            val imageView = ImageView(parent.context)
            return FlowerViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    var flowers: List<Item> = emptyList()
        set(value) {
            field = value
            adapter.submitList(value.toList())
        }

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutManager = GridLayoutManager(context, 2)
        setAdapter(adapter)
    }

    data class Item(
        @DrawableRes val drawableResId: Int,
        val onClick: (View) -> Unit
    )

    private class FlowerViewHolder(val imageView: ImageView) : ViewHolder(imageView) {
        init {
            imageView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            imageView.adjustViewBounds = true
        }

        fun bind(item: Item) {
            imageView.setImageResource(item.drawableResId)
            imageView.setOnClickListener(item.onClick)
        }
    }
}
