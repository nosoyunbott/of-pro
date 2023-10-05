package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.ar.of_pro.R

class ProposalFragment : Fragment() {

    lateinit var v : View
    lateinit var btnProposal : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_proposal, container, false)
        btnProposal = v.findViewById(R.id.btnProposal)
        return v
    }

    override fun onStart() {
        super.onStart()
        btnProposal.setOnClickListener{
            val action =
                ProposalFragmentDirections.actionProposalFragmentToRequestListProviderFragment()
            v.findNavController().navigate(action)
        }
    }

}