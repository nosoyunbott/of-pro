package com.ar.of_pro.fragments.provider

import android.content.Context
import android.os.Bundle
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
import com.bumptech.glide.Glide
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
    lateinit var imageUrl: String
    lateinit var imageView: ImageView
    lateinit var imageView2: ImageView
    lateinit var imageView3: ImageView
    private val db = FirebaseFirestore.getInstance()

    var isActive: Boolean = true

    val listOfImages = listOf(
        "https://images.unsplash.com/photo-1682686581030-7fa4ea2b96c3?auto=format&fit=crop&q=80&w=2070&ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1695849118500-c8034bc651b6?auto=format&fit=crop&q=80&w=1974&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1698332329329-6968a8ebc2e9?auto=format&fit=crop&q=80&w=1935&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    )
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
        imageView = v.findViewById(R.id.requestImageView)
        imageView2 = v.findViewById(R.id.imageView2)
        imageView3 = v.findViewById(R.id.imageView4)


        imageViewsList.add(imageView)
        imageViewsList.add(imageView2)
        imageViewsList.add(imageView3)

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
        txtTitle.text = request.requestTitle
        txtOcupation.text = request.categoryOcupation
        txtServiceType.text = request.categoryService
        txtTime.text = request.date
        txtPricing.text = request.maxCost.toString()
        txtDescription.text = request.description
        imageUrl = request.imageUrl

        btnProposal.setOnClickListener {

            val users = db.collection("Users")
            users.get().addOnSuccessListener { querySnapshot -> //ESTE LISTENER ES PARA LA DEMO
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
                )  // Retrieve the 'userType' attribute from SharedPreferences
                val idRequest = request.requestId//TODO Mandar idRequest desde la sesión
                val p = Proposal(
                    idProvider,
                    idRequest,
                    bid,
                    commentary
                )

                val newDocProposal = db.collection("Proposals").document()
                db.collection("Proposals").document(newDocProposal.id).set(p)
                //update request in BD
                RequestsService.updateProposalsQtyFromId(
                    request.requestId,
                    request.requestBidAmount
                )

                val action =
                    //agregar que edit text carguen el objeto a la db y crear entity Proposal
                    ProposalFragmentDirections.actionProposalFragmentToRequestsListFragment()
                v.findNavController().navigate(action)
            }


        }


    }


}