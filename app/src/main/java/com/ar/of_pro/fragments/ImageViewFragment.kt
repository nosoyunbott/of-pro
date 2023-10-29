package com.ar.of_pro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ar.of_pro.R
import com.bumptech.glide.Glide

class ImageViewFragment : Fragment() {
    lateinit var v: View
    lateinit var imageView : ImageView
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_image_view, container, false)
        imageView = v.findViewById(R.id.imageLarge)
        return v
    }

    override fun onStart() {
        super.onStart()
        val imageUrl = ImageViewFragmentArgs.fromBundle(requireArguments()).imageUrl
        Glide.with(requireContext())
            .load(imageUrl)
            .into(imageView);
    }
}