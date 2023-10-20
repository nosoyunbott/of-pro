package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.ar.of_pro.R
import com.ar.of_pro.services.RequestsService
import com.ar.of_pro.services.UserService
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RequestFragmentProccessFinish : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    val usersCollection=db.collection("Users")

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
        val request = ProposalFragmentArgs.fromBundle(requireArguments()).request

        val v = inflater.inflate(R.layout.fragment_request_proccess_finish_client, container, false)

        // Find references to your button and component

        val finishButton: Button = v.findViewById<Button>(R.id.finishButton)
        val ratingBar: RatingBar = v.findViewById<RatingBar>(R.id.ratingBar)


        // Set an OnClickListener for the finishButton
        finishButton.setOnClickListener {
            // Hide the button
            finishButton.visibility = View.GONE

            // Show the component
            ratingBar.visibility = View.VISIBLE

            // Add any additional logic or actions you need here
        }

        /*
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            ratingBar.onRatingBarChangeListener.onRatingChanged(this,v

            ){}
        }
        */

        fun handleRatingFinishRequest(rating: Float) {



           RequestsService.updateRequestState("FINALIZADA",request.requestId)
            UserService.updateRatingOfUser(rating,request.clientId)
            //ir a historial . Ver de hacer el pop.
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                // Call the custom function to handle the rating change
                handleRatingFinishRequest(rating)
            }
        }

        val fullNameTextView = v.findViewById<TextView>(R.id.fullNameTextView)
        val zoneTextView = v.findViewById<TextView>(R.id.zoneTextView)
        val rankingTextView = v.findViewById<TextView>(R.id.rankingTextView)
        val legendTextView = v.findViewById<TextView>(R.id.legendTextView)
        val paragraphTextView = v.findViewById<TextView>(R.id.paragraphTextView)
        val bigLegendTextView = v.findViewById<TextView>(R.id.bigLegendTextView)

        requestsCollection.document(request.requestId).get().addOnSuccessListener { document ->
            // Populate the views with data from the Request object

            legendTextView.text =
                document.getString("categoryOcupation") + " " + document.getString("categoryService")
            paragraphTextView.text = document.getString("description") ?: "" // Set the description
            bigLegendTextView.text = document.getLong("maxCost")
                .toString() + " " + document.getString("requestTitle") // Set the big legend

            usersCollection.document(request.clientId).get().addOnSuccessListener { userDocument ->
                fullNameTextView.text = userDocument.getString("name") + " " + userDocument.getString("lastName") // Client
                zoneTextView.text = userDocument.getString("location")//Client
                rankingTextView.text = userDocument.getDouble("rating").toString() // Client

            }


        }
        return v
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