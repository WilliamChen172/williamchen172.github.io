package com.example.artsyapp.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artsyapp.databinding.CardArtworkBinding
import com.example.artsyapp.model.Artwork

class ArtworkAdapter(private val context: Context, private val onClickListener: OnClickListener):
    RecyclerView.Adapter<ArtworkViewHolder>() {
    var artworks = mutableListOf<Artwork>()
    private val TAG = "ArtworkAdapter"

    fun setArtwork(artworks: List<Artwork>) {
        this.artworks = artworks.toMutableList()
        Log.d(TAG, "this is run")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardArtworkBinding.inflate(inflater, parent, false)
        return ArtworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        val artwork = artworks[position]
        holder.binding.artworkTitleText.text = artwork.title
        Glide.with(context)
            .asBitmap()
            .centerCrop()
            .load(artwork.thumbnail)
            .into(holder.binding.artworkImg);
        holder.binding.artworkImg.setOnClickListener {
            onClickListener.onClick(artwork)
        }
    }

    override fun getItemCount(): Int {
        return this.artworks.size
    }

    class OnClickListener(val clickListener: (artwork: Artwork) -> Unit) {
        fun onClick(artwork: Artwork) = clickListener(artwork)
    }
}

class ArtworkViewHolder(var binding: CardArtworkBinding): RecyclerView.ViewHolder(binding.root){
}