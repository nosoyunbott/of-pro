package com.ar.of_pro.fragments.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ar.of_pro.R
import com.ar.of_pro.fragments.provider.ProposalFragmentArgs
import com.ar.of_pro.services.RequestsService
import com.ar.of_pro.services.UserService
import com.google.firebase.firestore.FirebaseFirestore

class RequestProcessFinishClientFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    val usersCollection = db.collection("Users")


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


            RequestsService.updateRequestState("FINALIZADA", request.requestId)
            UserService.updateRatingOfUser(rating, request.clientId)
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
                fullNameTextView.text =
                    userDocument.getString("name") + " " + userDocument.getString("lastName") // Client
                zoneTextView.text = userDocument.getString("location")//Client
                rankingTextView.text = userDocument.getDouble("rating").toString() // Client

            }

        }
        return v
    }

}