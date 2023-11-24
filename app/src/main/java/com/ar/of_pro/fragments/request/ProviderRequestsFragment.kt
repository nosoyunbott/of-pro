package com.ar.of_pro.fragments.request

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.ProposalInformationAdapter
import com.ar.of_pro.entities.Proposal
import com.ar.of_pro.entities.ProposalInformation
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.User
import com.ar.of_pro.fragments.provider.ProposalFragmentArgs
import com.ar.of_pro.listeners.OnProposalInformationClickedListener
import com.ar.of_pro.models.UserModel
import com.ar.of_pro.services.ProposalsService
import com.ar.of_pro.services.RequestsService
import com.ar.of_pro.services.UserService
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class ProviderRequestsFragment : Fragment(), OnProposalInformationClickedListener {

    lateinit var v: View
    lateinit var recProviderList: RecyclerView
    var providerList: MutableList<ProposalInformation> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var proposalInformationAdapter: ProposalInformationAdapter

    val db = FirebaseFirestore.getInstance()
    val proposalsCollection = db.collection("Proposals")
    val usersCollection = db.collection("Users")

    lateinit var txtTitle: TextView

    lateinit var request: Request

    lateinit var userObj: User
    lateinit var proposalInfo: ProposalInformation
    lateinit var btnDelete: Button
    lateinit var btnEdit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        request = ProposalFragmentArgs.fromBundle(requireArguments()).request
        lifecycleScope.launch {
            val proposals = ProposalsService.getProposalsByRequestId(request.requestId)
           for(proposal in proposals){
               UserService.getUserById(proposal.providerId) { document, exception ->
                   if (exception == null && document != null) {
                       val user = document.toObject(UserModel::class.java)
                       if (user != null) {
                           proposalInfo = ProposalInformation(
                               user.name,
                               proposal.bid.toFloat(),
                               user.rating,
                               proposal.commentary,
                               user.ratingQuantity,
                               proposal.requestId,
                               proposal.providerId
                           )
                           Log.d("proposalInfo", proposalInfo.toString())
                           providerList.add(proposalInfo)

                           proposalInformationAdapter.notifyDataSetChanged()
                       }
                   } else {
                       Log.d("ErrorProfileEdit", "User not found")
                   }
               }
           }

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_provider_requests, container, false)
        txtTitle = v.findViewById(R.id.txtTitle)
        recProviderList = v.findViewById(R.id.rec_providers)
        btnDelete = v.findViewById(R.id.btnDeleteRequest)
        btnEdit = v.findViewById(R.id.btnEditRequest)
        btnEdit.visibility = View.GONE
        return v
    }

    override fun onStart() {
        super.onStart()
        txtTitle.text = request.requestTitle

        recProviderList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recProviderList.layoutManager = linearLayoutManager
        proposalInformationAdapter = ProposalInformationAdapter(providerList, this)
        recProviderList.adapter = proposalInformationAdapter

        btnDelete.setOnClickListener {
            showConfirmationDialog()

        }
        lifecycleScope.launch {
            if (checkIfRequestHasProposals()) {
                btnEdit.visibility = View.VISIBLE
            }
        }
        btnEdit.setOnClickListener {
            val action =
                ProviderRequestsFragmentDirections.actionProviderRequestsFragmentToRequestEditFragment(
                    request.requestId
                )
            v.findNavController().navigate(action)
        }

    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Confirmacion")
            .setMessage("Estás seguro que desea eliminar la solicitud?")
            .setPositiveButton("Si") { dialog, which ->
                deleteRequest()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteRequest() {
        RequestsService.deleteRequestById(request.requestId)
        ProposalsService.deleteProposalsFromRequestId(request.requestId)
        v.findNavController().popBackStack(R.id.requestsListFragment, true)
    }

    override fun onViewItemDetail(proposalInformation: ProposalInformation) {
        val action =
            ProviderRequestsFragmentDirections.actionProviderRequestsFragmentToRequestDetailFragment(
                proposalInformation
            )
        v.findNavController().navigate(action)
        Snackbar.make(v, proposalInformation.name, Snackbar.LENGTH_SHORT).show()
    }

    private suspend fun checkIfRequestHasProposals(): Boolean {
        val proposals = ProposalsService.getProposalsByRequestId(request.requestId)
        return proposals.isEmpty()
    }

}