package com.ar.of_pro.fragments.provider

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.entities.Proposal
import com.ar.of_pro.entities.Request
import com.ar.of_pro.services.RequestsService
import com.ar.of_pro.utils.DateUtils
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore


class ProposalFragment : Fragment() {

    lateinit var v: View
    lateinit var btnProposal: Button
    lateinit var txtTitle: TextView
    lateinit var txtOcupation: TextView
    lateinit var txtServiceType: TextView
    lateinit var txtTime: TextView
    lateinit var txtPricing: TextView
    lateinit var txtDescription: TextView
    lateinit var edtBudget: EditText
    lateinit var edtComment: EditText
    lateinit var listOfImages: MutableList<String>
    lateinit var imageView: ImageView
    lateinit var imageView2: ImageView
    lateinit var imageView3: ImageView
    lateinit var errorMessageTextView: TextView


    //Header
    lateinit var proposalProfileHeader: View
    lateinit var txtFullName: TextView
    lateinit var txtLocation: TextView
    lateinit var txtRating: TextView
    lateinit var txtRatingQuantity: TextView
    lateinit var imgHeader: ImageView

    private val db = FirebaseFirestore.getInstance()

    //flag para recargar el fragment
    var isActive: Boolean = true


    var imageViewsList: MutableList<ImageView> = mutableListOf()
    lateinit var request: Request

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_proposal, container, false)
        btnProposal = v.findViewById(R.id.btnProposal)
        txtTitle = v.findViewById(R.id.txtTitle)
        txtOcupation = v.findViewById(R.id.txtOcupation)
        txtServiceType = v.findViewById(R.id.txtServiceType)
        txtTime = v.findViewById(R.id.txtTime)
        txtPricing = v.findViewById(R.id.txtPricing)
        txtDescription = v.findViewById(R.id.txtBioDescription)
        edtBudget = v.findViewById(R.id.edtBudget)
        edtComment = v.findViewById(R.id.edtComment)
        errorMessageTextView = v.findViewById(R.id.errorMessageTextView)
        imageView = v.findViewById(R.id.requestImageView)
        imageView2 = v.findViewById(R.id.imageView2)
        imageView3 = v.findViewById(R.id.imageView4)
        imageViewsList.add(imageView)
        imageViewsList.add(imageView2)
        imageViewsList.add(imageView3)
        proposalProfileHeader = v.findViewById(R.id.proposalProfileHeader)
        txtFullName = proposalProfileHeader.findViewById(R.id.headerFullName)
        txtLocation = proposalProfileHeader.findViewById(R.id.headerLocation)
        txtRating = proposalProfileHeader.findViewById(R.id.headerRating)
        txtRatingQuantity = proposalProfileHeader.findViewById(R.id.headerRatingQuantity)
        imgHeader = proposalProfileHeader.findViewById(R.id.headerImage)
        return v
    }

    private fun setUpImageListener() {
        for ((index, value) in listOfImages.withIndex()) {
            Glide.with(requireContext())
                .load(value)
                .dontAnimate()
                .into(imageViewsList[index]);
            imageViewsList[index].setOnClickListener {
                val enlargeImageOnTap =
                    ProposalFragmentDirections.actionProposalFragmentToImageViewFragment(value)
                v.findNavController().navigate(enlargeImageOnTap)

            }
        }
    }

    override fun onStop() {
        super.onStop()
        isActive = false
    }

    override fun onResume() {
        super.onResume()
        setUpImageListener()
        if (!isActive) {
            v.findNavController()
                .navigate(ProposalFragmentDirections.actionProposalFragmentSelf(request))
            isActive = false
        }

    }

    override fun onStart() {
        super.onStart()
        request = ProposalFragmentArgs.fromBundle(requireArguments()).request
        txtTitle.text = "Trabajo: ${request.requestTitle}"
        txtOcupation.text = "Se solicita: ${request.categoryOcupation}"
        txtServiceType.text = "Tipo: ${request.categoryService}"
        txtTime.text = "Fecha: ${DateUtils.GetFormattedDate(request.date)}"

        txtPricing.text = "Precio: $" + request.maxCost.toString()
        txtDescription.text = "Detalle del pedido: ${request.description}"
        listOfImages = request.imageUrlArray
        val context = context

        val users = db.collection("Users")
        users.whereEqualTo(FieldPath.documentId(), request.clientId).get()
            .addOnSuccessListener { querySnapshot ->
                for (snapshot in querySnapshot) {
                    val profileImage = snapshot.getString("imageUrl") ?: ""
                    Glide.with(context!!).load(profileImage).into(imgHeader)
                    val name = snapshot.getString("name") ?: ""
                    val surname = snapshot.getString("lastname") ?: ""
                    txtFullName.text = "$name $surname"
                    val location = snapshot.getString("location") ?: ""
                    txtLocation.text = location
                    txtRating.visibility = View.GONE
                    txtRatingQuantity.visibility = View.GONE
                }

            }

        btnProposal.setOnClickListener {

            val sharedPref = context?.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
            errorMessageTextView.visibility = View.GONE
            if (edtBudget.text.isNotEmpty() && request.maxCost > edtBudget.text.toString()
                    .toFloat()
            ) {
                val users = db.collection("Users")
                users.get().addOnSuccessListener { querySnapshot ->
                    val userIds = ArrayList<String>()
                    for (document in querySnapshot) {
                        val userId = document.id
                        userIds.add(userId)

                    }
                    val sharedPreferences =
                        requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)


                    val bid = edtBudget.text.toString().toFloat()
                    val commentary = edtComment.text.toString()
                    val idProvider = sharedPreferences.getString(
                        "clientId",
                        ""
                    )
                    val idRequest = request.requestId
                    val p = Proposal(
                        idProvider,
                        idRequest,
                        bid,
                        commentary
                    )

                    val newDocProposal = db.collection("Proposals").document()
                    db.collection("Proposals").document(newDocProposal.id).set(p)
                    RequestsService.updateProposalsQtyFromId(
                        request.requestId,
                        request.requestBidAmount
                    )

                    val action =
                        ProposalFragmentDirections.actionProposalFragmentToRequestsListFragment()
                    v.findNavController().navigate(action)
                }
            } else {
                edtBudget.error = "Error con el presupuesto"
                //errorMessageTextView.visibility = View.VISIBLE
                val handler = Handler()
                handler.postDelayed({
                    errorMessageTextView.visibility = View.GONE
                }, 2000)
//        }
            }

        }


    }


}