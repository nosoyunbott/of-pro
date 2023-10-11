package com.ar.of_pro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.ProposalInformationAdapter
import com.ar.of_pro.entities.Proposal
import com.ar.of_pro.entities.ProposalInformation
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.User
import com.ar.of_pro.listeners.OnProposalInformationClickedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


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
    var userName: String = ""
    var userRating: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        request = ProposalFragmentArgs.fromBundle(requireArguments()).request
        var providerId: String = ""
        var bid: Float = 0f
        val requestId = "WmU6lopxhoshV8j50mk5"
        getProposalsFromRequestId(request.requestId)

    }

    /**
     * Retrieves proposals from Firestore based on a given request ID and populates the
     * [providerList] with data obtained from Firestore.
     *
     * @param requestId The unique identifier of the request to fetch proposals for.
     */
    private fun getProposalsFromRequestId(requestId: String) {

        proposalsCollection.get().addOnSuccessListener { proposals ->

            for (proposal in proposals) {
                if(proposal.getString("requestId") == requestId) {
                    val bid = proposal.getLong("bid")?.toFloat()
                    val providerId = proposal.getString("providerId")!!
                    val commentary = proposal.getString("commentary")

                    val p = Proposal(providerId!!,requestId, bid!!, commentary!!)
                    getProviderFromProviderId(p)

                }
            }
        }.addOnFailureListener { Exception ->
            Log.d("Error getting documents:", Exception.toString())
        }



    }
    /**
     * Retrieves provider information based on the given provider ID and bid,
     * and adds the provider's data to the [providerList] for later display in the UI.
     *
     * @param id The unique identifier of the provider in Firestore.
     * @param bid The bid associated with the provider for a specific proposal.
     */
    private fun getProviderFromProviderId(proposal: Proposal)  {
        val userDoc = usersCollection.document(proposal.providerId)
        userDoc.get().addOnSuccessListener { user ->
            if(user != null){
                userObj = User(user.getString("fullName")!!, user.getDouble("rating")!!, user.id)
                proposalInfo = ProposalInformation(userObj.name, proposal.bid, userObj.rating, proposal.commentary, user.getLong("ratingsQuantity")?.toInt(), proposal.requestId, proposal.providerId  ) //user.id tiene que ser proposal.providerId
                providerList.add(proposalInfo)

                //TODO desranciar esto

                proposalInformationAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_provider_requests, container, false)
        txtTitle = v.findViewById(R.id.txtTitle)
        recProviderList = v.findViewById(R.id.rec_providers)
        return v
    }

    override fun onStart() {
        super.onStart()
        //val request = ProposalFragmentArgs.fromBundle(requireArguments()).request
        txtTitle.text = request.requestTitle

        recProviderList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recProviderList.layoutManager = linearLayoutManager
        proposalInformationAdapter = ProposalInformationAdapter(providerList, this)
        recProviderList.adapter = proposalInformationAdapter

    }

    override fun onViewItemDetail(proposalInformation: ProposalInformation) {
        val action = ProviderRequestsFragmentDirections.actionProviderRequestsFragmentToRequestDetailFragment(proposalInformation)
        v.findNavController().navigate(action)
        Snackbar.make(v, proposalInformation.name, Snackbar.LENGTH_SHORT).show()
    }


}