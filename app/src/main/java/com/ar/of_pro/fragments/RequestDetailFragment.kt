package com.ar.of_pro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.google.android.material.snackbar.Snackbar

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
        val poposalInfo = RequestDetailFragmentArgs.fromBundle(requireArguments()).proposalInformation
        btnAccept.setOnClickListener{

            //v.findNavController().popBackStack(v.findNavController().graph.startDestinationId, false)
            v.findNavController().navigate(action)
        }
    }
}