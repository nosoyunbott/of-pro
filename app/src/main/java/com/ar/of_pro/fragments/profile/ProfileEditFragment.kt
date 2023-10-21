package com.ar.of_pro.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.google.firebase.auth.FirebaseAuth


class ProfileEditFragment : Fragment() {

    lateinit var v : View
    lateinit var btnAccept: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        btnAccept = v.findViewById(R.id.btnAccept)
        return v
    }

    override fun onStart() {
        super.onStart()
        btnAccept.setOnClickListener{

            //TODO actualizar todos los datos en la db

            val action = ProfileEditFragmentDirections.actionProfileEditFragmentToProfileFragment()
            v.findNavController().navigate(action)
        }

    }

}