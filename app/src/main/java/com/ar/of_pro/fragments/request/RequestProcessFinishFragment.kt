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
    lateinit var fullNameTextView: TextView
    lateinit var zoneTextView: TextView
    lateinit var rankingTextView: TextView
    lateinit var legendTextView: TextView
    lateinit var paragraphTextView: TextView
    lateinit var requestTitle: TextView
    lateinit var profilePicture: ImageView
    lateinit var mediumLegendTextView: TextView
    lateinit var favIcon: ImageView
    lateinit var userTypeTitle: TextView
    lateinit var txtPrice: TextView
    lateinit var txtCategoryService : TextView
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
                //Toast.makeText(requireContext(), "PUNTUACIÓN: $rating", Toast.LENGTH_SHORT).show()

                // Navigate back to the previous screen (pop the current fragment)
                val action=RequestProcessFinishFragmentDirections.actionRequestFragmentProccessFinishClientToRequestsHistoryFragment()
                findNavController().navigate(action)
            }

            Toast.makeText(requireContext(), "PUNTUACIÓN: $rating", Toast.LENGTH_SHORT).show()
//            val action =
//                RequestProcessFinishFragmentDirections.actionRequestFragmentProccessFinishClientToRequestsHistoryFragment()
//            findNavController().navigate(action)

        }

        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                handleRatingFinishRequest(rating)
            }
        }

        fullNameTextView = v.findViewById<TextView>(R.id.fullNameTextView)
        zoneTextView = v.findViewById<TextView>(R.id.zoneTextView)
        rankingTextView = v.findViewById<TextView>(R.id.rankingTextView)
        legendTextView = v.findViewById<TextView>(R.id.legendTextView)
        paragraphTextView = v.findViewById<TextView>(R.id.paragraphTextView)
        requestTitle = v.findViewById<TextView>(R.id.bigLegendTextView)
        profilePicture = v.findViewById<ImageView>(R.id.photoImageView);
        mediumLegendTextView = v.findViewById<TextView>(R.id.mediumLegendTextView)
        favIcon = v.findViewById<ImageView>(R.id.fav_star_icon)
        userTypeTitle = v.findViewById(R.id.userTypeTitle)
        txtPrice = v.findViewById(R.id.txtPrice)
        txtCategoryService = v.findViewById(R.id.txtCategorySercvice)

        requestsCollection.document(request.requestId).get().addOnSuccessListener { document ->
            if(userType == "CLIENT"){
                userTypeTitle.text = "Datos del proovedor"
            }else{
                userTypeTitle.text = "Datos del cliente"
            }
            legendTextView.text = "Disciplina: " +
                document.getString("categoryOcupation")
            txtCategoryService.text = "Categoria: " + document.getString("categoryService")
            paragraphTextView.text = "Descripción: " + document.getString("description") ?: "" // Set the description

            requestTitle.text = document.getString("requestTitle") // Set the big legend
            txtPrice.text = "$" + document.getLong("maxCost")
                .toString()
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



                if (userType == "PROVIDER" || request.state=="FINALIZADA") {
                    mediumLegendTextView.visibility = View.VISIBLE
                    mediumLegendTextView.text = "Estado de la solicitud: " + request.state
                    finishbutton.visibility = View.GONE
                    rankingTextView.visibility = View.GONE
                    favIcon.visibility = View.GONE
                } else if (request.state != "FINALIZADA" && userType == "CLIENT") {
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