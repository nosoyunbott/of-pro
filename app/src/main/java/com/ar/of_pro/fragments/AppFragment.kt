package com.ar.of_pro.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.ar.of_pro.R

class AppFragment : Fragment() {

    lateinit var v : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_app, container, false)

        Handler().postDelayed({
            if(false){
                val action = AppFragmentDirections.actionAppFragmentToRequestsListFragment()
                v.findNavController().navigate(action)
            }else{
                val action = AppFragmentDirections.actionAppFragmentToRequestListProviderFragment()
                v.findNavController().navigate(action)
            }
        },500)

        return v
    }

}