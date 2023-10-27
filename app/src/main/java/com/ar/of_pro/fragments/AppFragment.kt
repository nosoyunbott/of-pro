package com.ar.of_pro.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R

class AppFragment : Fragment() {

    lateinit var v : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.fragment_app, container, false)


        Handler().postDelayed({
                val action = AppFragmentDirections.actionAppFragmentToRequestsListFragment()
                v.findNavController().navigate(action)

        },500)



        return v
    }

}