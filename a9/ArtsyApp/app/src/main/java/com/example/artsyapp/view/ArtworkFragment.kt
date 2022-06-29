package com.example.artsyapp.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.artsyapp.R
import com.example.artsyapp.databinding.FragmentArtworkBinding
import com.example.artsyapp.model.Artwork
import com.example.artsyapp.viewmodel.ArtistInfoViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class ArtworkFragment : Fragment() {
    private lateinit var binding: FragmentArtworkBinding
    private lateinit var artworks: List<Artwork>
    private val viewModel: ArtistInfoViewModel by activityViewModels()
    private lateinit var adapter: ArtworkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ArtworkAdapter(requireContext(), ArtworkAdapter.OnClickListener { artwork ->
//            val dialog = Dialog(requireContext())
            val gene = viewModel.getCategory(artwork.id)
            gene.observe(this, Observer { gene ->
                val builder = AlertDialog.Builder(view?.rootView!!.context)
                val categoryView = LayoutInflater.from(view?.rootView!!.context).inflate(R.layout.category_dialog, null)
                val emptyView = LayoutInflater.from(view?.rootView!!.context).inflate(R.layout.empty_dialog, null)
                if (!gene.isEmpty()) {
                    val category = categoryView.findViewById<View>(R.id.category_text) as TextView
                    val description = categoryView.findViewById<View>(R.id.description_text) as TextView
                    val image: ImageView = categoryView.findViewById<View>(R.id.category_img) as ImageView

                    category.text = gene[0].name
                    description.text = gene[0].description
                    Glide.with(requireContext())
                        .asBitmap()
                        .centerCrop()
                        .load(gene[0].thumbnail)
                        .into(image);

                    builder.setView(categoryView)
                } else {
                    builder.setView(emptyView)
                }
                builder.setCancelable(true)
                val dialog = builder.create()
                dialog.show()
//                val lp = WindowManager.LayoutParams()
//                lp.copyFrom(dialog.getWindow()!!.getAttributes())
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//                dialog.window?.attributes = lp
            })
        })

        val artworkString = requireArguments().getString("artworks")
        artworks = Json.decodeFromString(artworkString!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ArtworkFragment", "Fragment view created")
        binding = FragmentArtworkBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.artworkView.adapter = adapter
        binding.artworkView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.setArtwork(artworks)
        if (!artworks.isEmpty()) {
            binding.noartworkView.visibility = View.INVISIBLE
        }
    }





}