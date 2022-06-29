package com.example.artsyapp.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.artsyapp.databinding.RowFavoriteBinding
import com.example.artsyapp.model.Favorite

class FavoriteAdapter(private val onClickListener: OnClickListener):
    RecyclerView.Adapter<FavoriteViewHolder>() {
    var artists = mutableListOf<Favorite>()
    private val TAG = "FavoriteAdapter"

    fun setFavorite(favorites: List<Favorite>) {
        this.artists = favorites.toMutableList()
        Log.d(TAG, this.artists.size.toString())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowFavoriteBinding.inflate(inflater, parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val artist = artists[position]
        holder.binding.artistNameText.text = artist.name
        holder.binding.artistNationText.text = artist.nationality
        holder.binding.artistBirthText.text = artist.birthday
        Log.d(TAG, "this is run")
        holder.binding.openDetailsBtn.setOnClickListener {
            onClickListener.onClick(artist)
        }
    }

    override fun getItemCount(): Int {
        return this.artists.size
    }

    class OnClickListener(val clickListener: (favorite: Favorite) -> Unit) {
        fun onClick(favorite: Favorite) = clickListener(favorite)
    }
}

class FavoriteViewHolder(var binding: RowFavoriteBinding): RecyclerView.ViewHolder(binding.root){
}