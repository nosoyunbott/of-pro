package com.ar.of_pro.fragments.request

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.services.RequestsService

class RequestDetailFragment : Fragment() {
    lateinit var v: View
    lateinit var btnAccept: Button
    lateinit var btnDeny: Button

    lateinit var txtDesc: TextView
    lateinit var txtBid: TextView
    lateinit var profileHeader: View
    lateinit var txtName: TextView
    lateinit var txtLocation: TextView
    lateinit var txtCalification: TextView
    lateinit var txtCalificationQty: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_detail, container, false)
        btnAccept = v.findViewById(R.id.btnAccept)
        btnDeny= v.findViewById(R.id.btnDeny)
        txtDesc = v.findViewById(R.id.txtDesc)
        txtBid = v.findViewById(R.id.txtBid)
        profileHeader = v.findViewById(R.id.proposalProfileHeader)
        txtName = profileHeader.findViewById(R.id.txtFullName)
        txtLocation = profileHeader.findViewById(R.id.txtLocation)
        txtCalification = profileHeader.findViewById(R.id.txtCalification)
        txtCalificationQty = profileHeader.findViewById(R.id.txtCalificationQty)
        return v
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        val action =
            RequestDetailFragmentDirections.actionRequestDetailFragmentToRequestsListFragment()
        val proposalInfo = RequestDetailFragmentArgs.fromBundle(
            requireArguments()
        ).proposalInformation
        txtDesc.text = proposalInfo.commentary
        txtBid.text = "$${proposalInfo.bidAmount}"
        txtName.text = proposalInfo.name
        //proposalInfo.calification se esta truncando ¿como mostrar el dato bien?
        txtCalification.text = "${proposalInfo.calification} ${resources.getString(R.string.star)}"
        txtCalificationQty.text = proposalInfo.calificationQty.toString()

        btnAccept.setOnClickListener{
            RequestsService.updateProviderIdFromRequest(proposalInfo.requestId, proposalInfo.providerId)

            v.findNavController().popBackStack(v.findNavController().graph.startDestinationId, false)
            //v.findNavController().navigate(action)
        }
    }
}