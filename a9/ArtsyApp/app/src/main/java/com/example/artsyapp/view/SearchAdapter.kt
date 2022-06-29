package com.example.artsyapp.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artsyapp.R
import com.example.artsyapp.databinding.CardArtistBinding
import com.example.artsyapp.model.Artist

class SearchAdapter(private val context: Context, private val onClickListener: OnClickListener):
    RecyclerView.Adapter<SearchViewHolder>() {
        var artists = mutableListOf<Artist>()
        private val TAG = "SearchAdapter"

        fun setResults(artists: List<Artist>) {
            this.artists = artists.toMutableList()
            Log.d(TAG, "this is run")
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CardArtistBinding.inflate(inflater, parent, false)
            return SearchViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val artist = artists[position]
            Log.d(TAG, artist.toString())
            holder.binding.searchArtistName.text = artist.name
            if (artist.thumbnail == "/assets/shared/missing_image.png") {
                holder.binding.searchImg.setImageResource(R.drawable.artsy_logo)
            } else {
                Glide.with(context)
                    .asBitmap()
                    .centerCrop()
                    .load(artist.thumbnail)
                    .into(holder.binding.searchImg)
            }
            holder.binding.artistCard.setOnClickListener {
                onClickListener.onClick(artist)
            }
        }

        override fun getItemCount(): Int {
            return this.artists.size
        }

        class OnClickListener(val clickListener: (artist: Artist) -> Unit) {
            fun onClick(artist: Artist) = clickListener(artist)
        }
    }

class SearchViewHolder(var binding: CardArtistBinding): RecyclerView.ViewHolder(binding.root){
}