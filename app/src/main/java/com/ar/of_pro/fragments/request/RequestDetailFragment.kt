package com.ar.of_pro.fragments.request

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.services.RequestsService
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

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
    lateinit var imgHeader: ImageView

    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_detail, container, false)
        btnAccept = v.findViewById(R.id.btnAccept)
        btnDeny = v.findViewById(R.id.btnDeny)
        txtDesc = v.findViewById(R.id.txtDesc)
        txtBid = v.findViewById(R.id.txtBid)
        profileHeader = v.findViewById(R.id.proposalProfileHeader)
        txtName = profileHeader.findViewById(R.id.headerFullName)
        txtLocation = profileHeader.findViewById(R.id.headerLocation)
        txtCalification = profileHeader.findViewById(R.id.headerRating)
        txtCalificationQty = profileHeader.findViewById(R.id.headerRatingQuantity)
        imgHeader = profileHeader.findViewById(R.id.headerImage)
        return v
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        val proposalInfo = RequestDetailFragmentArgs.fromBundle(
            requireArguments()
        ).proposalInformation
        txtDesc.text = proposalInfo.commentary
        txtBid.text = "$${proposalInfo.bidAmount}"
        txtName.text = proposalInfo.name
        //proposalInfo.calification se esta truncando ¿como mostrar el dato bien?
        txtCalification.text = "${proposalInfo.calification} ${resources.getString(R.string.star)}"
        txtCalificationQty.text = " (${proposalInfo.calificationQty})"

        val users = db.collection("Users")
        users.whereEqualTo(FieldPath.documentId(), proposalInfo.providerId).get()
            .addOnSuccessListener { querySnapshot ->
                for (snapshot in querySnapshot) {

                    val profileImage = snapshot.getString("imageUrl") ?: ""
                    Glide.with(requireContext()).load(profileImage).into(imgHeader)
                    val location = snapshot.getString("location") ?: ""
                    txtLocation.text = location
                }

            }

        btnAccept.setOnClickListener {
            RequestsService.updateProviderIdFromRequest(
                proposalInfo.requestId,
                proposalInfo.providerId
            )

            v.findNavController()
                .popBackStack(v.findNavController().graph.startDestinationId, false)
            //v.findNavController().navigate(action)
        }
    }
}