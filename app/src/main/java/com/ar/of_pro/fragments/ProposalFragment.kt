package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.ar.of_pro.R

class ProposalFragment : Fragment() {

    lateinit var v : View
    lateinit var btnProposal : Button
    lateinit var txtTitle : TextView
    lateinit var txtOcupation : TextView
    lateinit var txtServiceType : TextView
    lateinit var txtTime : TextView
    lateinit var txtPricing : TextView
    lateinit var txtDescription : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_proposal, container, false)
        btnProposal = v.findViewById(R.id.btnProposal)
        txtTitle = v.findViewById(R.id.txtTitle)
        txtOcupation = v.findViewById(R.id.txtOcupation)
        txtServiceType = v.findViewById(R.id.txtServiceType)
        txtTime = v.findViewById(R.id.txtTime)
        txtPricing = v.findViewById(R.id.txtPricing)
        txtDescription = v.findViewById(R.id.txtDescription)
        return v
    }

    override fun onStart() {
        super.onStart()

        val request = ProposalFragmentArgs.fromBundle(requireArguments()).request
        txtTitle.text = request.requestTitle
        txtOcupation.text = request.categoryOcupation
        txtServiceType.text = request.categoryService
        txtTime.text = request.date
        txtPricing.text = request.maxCost.toString()
        txtDescription.text = request.description

        btnProposal.setOnClickListener{
            val action =
                ProposalFragmentDirections.actionProposalFragmentToRequestListProviderFragment()
            v.findNavController().navigate(action)
        }
    }

}