package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ar.of_pro.R
import com.google.firebase.firestore.FirebaseFirestore

class RequestFragmentProccessFinishProvider : Fragment() {

    lateinit var v: View

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    val usersCollection=db.collection("Users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val request = ProposalFragmentArgs.fromBundle(requireArguments()).request


        v = inflater.inflate(
            R.layout.fragment_request_proccess_finish_provider,
            container,
            false
        )

        // Access views in your layout by their IDs
        val fullNameTextView = v.findViewById<TextView>(R.id.prfullNameTextView)
        val zoneTextView = v.findViewById<TextView>(R.id.zoneTextView)
        val rankingTextView = v.findViewById<TextView>(R.id.rankingTextView)
        val legendTextView = v.findViewById<TextView>(R.id.legendTextView)
        val paragraphTextView = v.findViewById<TextView>(R.id.paragraphTextView)
        val bigLegendTextView = v.findViewById<TextView>(R.id.bigLegendTextView)

        requestsCollection.document(request.requestId).get().addOnSuccessListener { document ->
            // Populate the views with data from the Request object

            legendTextView.text = document.getString("categoryOcupation") + " " + document.getString("categoryService")
            paragraphTextView.text = document.getString("description")?: "" // Set the description
            bigLegendTextView.text = document.getLong("maxCost").toString() + " " +document.getString("requestTitle") // Set the big legend

            usersCollection.document(request.clientId).get().addOnSuccessListener { userDocument->
                fullNameTextView.text = userDocument.getString("fullName") // Client
                zoneTextView.text = userDocument.getString("location")//Client
                rankingTextView.text = userDocument.getDouble("rating").toString() // Client

            }
        }

        return v


    }



    override fun onStart() {
        super.onStart()






    }

    //TODO elaborar finalizacion del contrato y pasar estado a FINALIZADO @Moragues
}