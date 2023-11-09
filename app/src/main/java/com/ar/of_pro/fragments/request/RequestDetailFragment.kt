package com.ar.of_pro.fragments.request

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.models.UserModel
import com.ar.of_pro.services.RequestsService
import com.ar.of_pro.services.UserService
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.round
import kotlin.math.truncate

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
        if (proposalInfo.calificationQty > 0) {
            val calif = proposalInfo.calification / proposalInfo.calificationQty
            val truncatedCalif = String.format("%.1f", calif)
            txtCalification.text = "$truncatedCalif ${resources.getString(R.string.star)}"
        } else {
            txtCalification.text =
                "${proposalInfo.calification} ${resources.getString(R.string.star)}"
        }
        txtCalificationQty.text = proposalInfo.calificationQty.toString()

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
//        UserService.getUserById(proposalInfo.providerId){document, exception ->
//            if(exception==null && document!= null){
//                val user = document.toObject(UserModel::class.java)
//                if(user!=null){
//                    Glide.with(requireContext()).load(user.).into(imgHeader)
//                }
//            }
//
//
//        }
        btnAccept.setOnClickListener {
            RequestsService.updateProviderIdFromRequest(
                proposalInfo.requestId,
                proposalInfo.providerId
            )

            v.findNavController()
                .popBackStack(v.findNavController().graph.startDestinationId, false)
            //v.findNavController().navigate(action)
        }

        btnDeny.setOnClickListener {

            val proposalsCollection = db.collection("Proposals")

            // Busca el documento con el campo providerId igual a la variable providerId
            proposalsCollection.whereEqualTo("requestId", proposalInfo.requestId)
                .whereEqualTo("providerId", proposalInfo.providerId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {

                        Log.d("document.id", document.id)
                        document.reference.delete()
                            .addOnSuccessListener {
                                Log.d("Firestore", "Documento eliminado correctamente")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error al eliminar el documento", e)
                            }

                        val requestsCollection = db.collection("Requests")
                        requestsCollection.whereEqualTo(
                            FieldPath.documentId(),
                            proposalInfo.requestId
                        ).get().addOnSuccessListener { querySnapshot ->
                            for (document in querySnapshot) {
                                var reqBidAmount =
                                    document.getLong("requestBidAmount")?.toInt() ?: 0
                                RequestsService.updateRequestBidAmount(
                                    proposalInfo.requestId,
                                    reqBidAmount
                                )

                                v.findNavController()
                                    .popBackStack(
                                        v.findNavController().graph.startDestinationId,
                                        false
                                    )

                            }

                        }

                    }
                }


            //TODO borrar proposal por UID
        }
    }
}