package com.ar.of_pro.fragments.request

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.fragments.provider.ProposalFragmentArgs
import com.ar.of_pro.services.RequestsService
import com.ar.of_pro.services.UserService
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class RequestProcessFinishFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    val usersCollection = db.collection("Users")

    lateinit var finishbutton: Button
    lateinit var ratingBar: RatingBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val request = ProposalFragmentArgs.fromBundle(requireArguments()).request

        val v = inflater.inflate(R.layout.fragment_request_proccess_finish_client, container, false)
        finishbutton = v.findViewById(R.id.finishButton)
        ratingBar = v.findViewById(R.id.ratingBar)
        val sharedPref = context?.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val userType = sharedPref!!.getString("userType", "")
        finishButtonReview()

        fun handleRatingFinishRequest(rating: Float) {

            RequestsService.updateRequestState("FINALIZADA", request.requestId)

            requestsCollection.document(request.requestId).get().addOnSuccessListener { document ->

                val providerId = document.getString("providerId")
                UserService.updateRatingOfUser(rating, providerId!!)
                Toast.makeText(requireContext(), "PUNTUACIÓN: $rating", Toast.LENGTH_SHORT).show()

                // Navigate back to the previous screen (pop the current fragment)
                val action=RequestProcessFinishFragmentDirections.actionRequestFragmentProccessFinishClientToRequestsHistoryFragment()
                findNavController().navigate(action)
            }

            Toast.makeText(requireContext(), "PUNTUACIÓN: $rating", Toast.LENGTH_SHORT).show()
            val action =
                RequestProcessFinishFragmentDirections.actionRequestFragmentProccessFinishClientToRequestsHistoryFragment()
            findNavController().navigate(action)

        }

        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                handleRatingFinishRequest(rating)
            }
        }

        val fullNameTextView = v.findViewById<TextView>(R.id.fullNameTextView)
        val zoneTextView = v.findViewById<TextView>(R.id.zoneTextView)
        val rankingTextView = v.findViewById<TextView>(R.id.rankingTextView)
        val legendTextView = v.findViewById<TextView>(R.id.legendTextView)
        val paragraphTextView = v.findViewById<TextView>(R.id.paragraphTextView)
        val bigLegendTextView = v.findViewById<TextView>(R.id.bigLegendTextView)
        var profilePicture = v.findViewById<ImageView>(R.id.photoImageView);
        val mediumLegendTextView = v.findViewById<TextView>(R.id.mediumLegendTextView)
        val favIcon = v.findViewById<ImageView>(R.id.fav_star_icon)


        requestsCollection.document(request.requestId).get().addOnSuccessListener { document ->
            legendTextView.text =
                document.getString("categoryOcupation") + " " + document.getString("categoryService")
            paragraphTextView.text = document.getString("description") ?: "" // Set the description

            bigLegendTextView.text = "$" + document.getLong("maxCost")
                .toString() + " " + document.getString("requestTitle") // Set the big legend

            var userId: String? = null;
            if (userType == "PROVIDER") {
                userId = document.getString("clientId") ?: ""
            } else if (userType == "CLIENT") {
                userId = document.getString("providerId") ?: ""
            }
            usersCollection.document(userId!!).get().addOnSuccessListener { userDocument ->
                val imageUrl = userDocument.getString("imageUrl") ?: ""
                profilePicture = profilePicture
                Glide.with(requireContext()).load(imageUrl).into(profilePicture);
                fullNameTextView.text =
                    userDocument.getString("name") + " " + userDocument.getString("lastName") // Client
                zoneTextView.text = userDocument.getString("location")//Client

                val ratingAmount=userDocument.getDouble("rating")
                val ratingQuantity=userDocument.getDouble("ratingQuantity")
                if(ratingQuantity?.toInt() !=0)
                {
                    val rating= ratingAmount!! / ratingQuantity!!;
                    rankingTextView.text = String.format("%.1f", rating) // Client
                }
                else
                {
                    rankingTextView.text="Sin calificaciones "
                }



                if (userType == "PROVIDER") {
                    mediumLegendTextView.visibility = View.VISIBLE
                    mediumLegendTextView.text = "Estado de la solicitud: " + request.state
                    finishbutton.visibility = View.GONE
                    rankingTextView.visibility = View.GONE
                    favIcon.visibility = View.GONE
                } else if (request.state == "FINALIZADA" && userType == "CLIENTE") {
                    finishbutton.visibility = View.VISIBLE
                    rankingTextView.visibility = View.VISIBLE
                    favIcon.visibility = View.VISIBLE
                }

            }

        }
        return v
    }

    fun finishButtonReview() {
        val request = ProposalFragmentArgs.fromBundle(requireArguments()).request
        val sharedPref = context?.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val userType = sharedPref!!.getString("userType", "")
        if (request.state != "FINALIZADA" && userType == "CLIENT") {
            finishbutton.setOnClickListener {
                finishbutton.visibility = View.GONE

                ratingBar.visibility = View.VISIBLE

            }
        }
    }

}