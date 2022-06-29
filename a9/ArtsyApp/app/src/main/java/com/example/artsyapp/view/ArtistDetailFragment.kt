package com.example.artsyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.Visibility
import com.example.artsyapp.databinding.FragmentArtistDetailBinding
import com.example.artsyapp.model.ArtistInfo
import com.example.artsyapp.viewmodel.ArtistInfoViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ArtistDetailFragment: Fragment() {
    private lateinit var binding: FragmentArtistDetailBinding
    private val viewModel: ArtistInfoViewModel by activityViewModels()
    private lateinit var info: ArtistInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val infoString = requireArguments().getString("info")
        info = Json.decodeFromString(infoString!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (info.name == "") {
            binding.nameKeyText.visibility = View.GONE
            binding.nameValueText.visibility = View.GONE
        }
        if (info.nationality == "") {
            binding.natiKeyText.visibility = View.GONE
            binding.natiValueText.visibility = View.GONE
        }
        if (info.birthday == "") {
            binding.birthKeyText.visibility = View.GONE
            binding.birthValueText.visibility = View.GONE
        }
        if (info.deathday == "") {
            binding.deathKeyText.visibility = View.GONE
            binding.deathValueText.visibility = View.GONE
        }
        if (info.biography == "") {
            binding.bioKeyText.visibility = View.GONE
            binding.bioValueText.visibility = View.GONE
        }
        binding.nameValueText.text = info.name
        binding.natiValueText.text = info.nationality
        binding.birthValueText.text = info.birthday
        binding.deathValueText.text = info.deathday
        binding.bioValueText.text = info.biography
    }
}