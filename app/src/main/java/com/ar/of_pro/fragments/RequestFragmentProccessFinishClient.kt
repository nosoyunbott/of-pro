package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.ar.of_pro.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RequestFragmentProccessFinish : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_request_inproccess_finish, container, false)

        // Find references to your button and component

        val finishButton: Button = rootView.findViewById<Button>(R.id.finishButton)
        val ratingBar: RatingBar = rootView.findViewById<RatingBar>(R.id.ratingBar)

        // Set an OnClickListener for the finishButton
        finishButton.setOnClickListener {
            // Hide the button
            finishButton.visibility = View.GONE

            // Show the component
            ratingBar.visibility = View.VISIBLE

            // Add any additional logic or actions you need here
        }

        return rootView
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            RequestFragmentProccessFinish().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}