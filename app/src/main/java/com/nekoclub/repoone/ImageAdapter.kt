package com.nekoclub.repoone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageAdapter(
    private var images: List<VaultImage>,
    private val onImageClick: (VaultImage) -> Unit,
    private val onImageLongClick: (VaultImage) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    
    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        
        Glide.with(holder.itemView.context)
            .load(image.file)
            .centerCrop()
            .into(holder.imageView)
        
        holder.itemView.setOnClickListener {
            onImageClick(image)
        }
        
        holder.itemView.setOnLongClickListener {
            onImageLongClick(image)
            true
        }
    }
    
    override fun getItemCount() = images.size
    
    fun updateImages(newImages: List<VaultImage>) {
        // Calculate diff on background thread for better performance
        CoroutineScope(Dispatchers.Default).launch {
            val diffCallback = ImageDiffCallback(images, newImages)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            
            withContext(Dispatchers.Main) {
                images = newImages
                diffResult.dispatchUpdatesTo(this@ImageAdapter)
            }
        }
    }
    
    private class ImageDiffCallback(
        private val oldList: List<VaultImage>,
        private val newList: List<VaultImage>
    ) : DiffUtil.Callback() {
        
        override fun getOldListSize() = oldList.size
        
        override fun getNewListSize() = newList.size
        
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
