package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ar.of_pro.R

class RequestFragmentProccessFinishProvider : Fragment() {

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(
            R.layout.fragment_request_proccess_finish_provider,
            container,
            false
        )

        return v
    }

    override fun onStart() {
        super.onStart()

        val request = ProposalFragmentArgs.fromBundle(requireArguments()).request


    }
}