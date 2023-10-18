package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.bumptech.glide.Glide


class ProviderProfileFragment : Fragment() {

    lateinit var v : View
    lateinit var btnEdit : Button
    lateinit var photo: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_provider_profile, container, false)
        btnEdit = v.findViewById(R.id.btnEdit)
        photo = v.findViewById(R.id.profilePicture)
        return v
    }

    override fun onStart() {
        super.onStart()
        Glide.with(requireContext())
            .load("content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000000033/ORIGINAL/NONE/image%2Fjpeg/850193783")
            .into(photo);
        btnEdit.setOnClickListener{
            val action = ProviderProfileFragmentDirections.actionProviderProfileFragmentToProviderProfileEditFragment()
            v.findNavController().navigate(action)
        }
    }

}