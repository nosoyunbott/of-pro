package com.ar.of_pro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.ServiceProviderAdapter
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.ServiceProvider
import com.ar.of_pro.entities.User
import com.ar.of_pro.listeners.OnServiceProviderClickedListener
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates


class ProviderRequestsFragment : Fragment(), OnServiceProviderClickedListener {

    lateinit var v: View
    lateinit var recProviderList: RecyclerView
    var providerList: MutableList<ServiceProvider> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var serviceProviderAdapter: ServiceProviderAdapter

    val db = FirebaseFirestore.getInstance()
    val proposalsCollection = db.collection("Proposals")
    val usersCollection = db.collection("Users")

    lateinit var userObj: User
    var userName: String = ""
    var userRating: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var providerId: String = ""
        var bid: Float = 0f
        val requestId = "QBFNQx2HbgRCXH5YV5tJ"
        getProposalsFromRequestId(requestId)

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
                    getProviderFromProviderId(providerId!!, bid!!)

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
    private fun getProviderFromProviderId(id: String, bid: Float)  {
        val userDoc = usersCollection.document(id)
        userDoc.get().addOnSuccessListener { user ->
            if(user != null){

                userObj = User(user.getString("fullName")!!, user.getLong("rating")?.toFloat()!!)
                providerList.add(ServiceProvider(userObj.name, userObj.rating, bid))

                //TODO desranciar esto

                serviceProviderAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_provider_requests, container, false)
        recProviderList = v.findViewById(R.id.rec_providers)
        return v
    }

    override fun onStart() {
        super.onStart()


        recProviderList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recProviderList.layoutManager = linearLayoutManager
        serviceProviderAdapter = ServiceProviderAdapter(providerList, this)
        recProviderList.adapter = serviceProviderAdapter

    }

    override fun onViewItemDetail(serviceProvider: ServiceProvider) {
        val action = ProviderRequestsFragmentDirections.actionProviderRequestsFragmentToRequestDetailFragment(serviceProvider)
        v.findNavController().navigate(action)
        Snackbar.make(v, serviceProvider.name, Snackbar.LENGTH_SHORT).show()
    }


}