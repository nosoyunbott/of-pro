package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.ar.of_pro.R

class RequestDetailFragment : Fragment() {
    lateinit var v: View
    lateinit var btnAccept: Button
    lateinit var btnDeny: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_detail, container, false)
        btnAccept = v.findViewById(R.id.btnAccept)
        btnDeny= v.findViewById(R.id.btnDeny)
        return v
    }

    override fun onStart() {
        super.onStart()
        val action = RequestDetailFragmentDirections.actionRequestDetailFragmentToRequestsListFragment()

        btnAccept.setOnClickListener{
            v.findNavController().navigate(action)
        }
    }
}