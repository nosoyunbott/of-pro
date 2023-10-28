package com.ar.of_pro.fragments.request

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.RequestCardAdapter
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.Request
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//TODO Actualizar cantidad de proposals para que figuren en el recycler view
//TODO  dejar la demo linda para el miercoles
class RequestsListFragment : Fragment(), OnViewItemClickedListener {


    lateinit var filterContainer: LinearLayout
    var ocupationList: List<String> = Ocupation().getList()

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    var requestList: MutableList<Request> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: RequestCardAdapter

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    val proposalsCollection = db.collection("Proposals")


    private suspend fun getProposals(): List<ProposalModel> {
        return try {
            val querySnapshot = proposalsCollection.get().await()

            val proposalsList = mutableListOf<ProposalModel>() // Replace with your request model

            for (document in querySnapshot) {
                val proposal = document.toObject(ProposalModel::class.java)
                proposalsList.add(proposal)
            }

            proposalsList // Return the list of requests

        } catch (e: Exception) {
            // Handle errors
            e.printStackTrace()
            emptyList() // Return an empty list or handle the error as needed
        }
    }
    private suspend fun getProposalsByRequestId(requestId: String): List<ProposalModel> {
        return try {
            val querySnapshot = proposalsCollection.get().await()

            val proposalsList = mutableListOf<ProposalModel>() // Replace with your request model

            for (document in querySnapshot) {
                val proposal = document.toObject(ProposalModel::class.java)
                if(proposal.requestId == requestId) {
                    proposalsList.add(proposal)
                }
            }

            proposalsList // Return the list of requests

        } catch (e: Exception) {
            // Handle errors
            e.printStackTrace()
            emptyList() // Return an empty list or handle the error as needed
        }
    }

    private suspend fun getRequests(): List<RequestModel> {
        return try {
            val querySnapshot = requestsCollection.get().await()

            val requestsList = mutableListOf<RequestModel>() // Replace with your request model


            for (document in querySnapshot) {

                //val request = document.toObject(RequestModel::class.java)
                val title = document.getString("requestTitle") ?: ""
                val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
                val selectedOcupation = document.getString("categoryOcupation") ?: ""
                val selectedServiceType = document.getString("categoryService") ?: ""
                val description = document.getString("description") ?: ""
                val state = document.getString("state") ?: ""
                val date = document.getString("date") ?: ""
                val maxCost = document.getLong("maxCost")?.toInt() ?: 0
                val clientId = document.getString("clientId") ?: ""
                val requestId = document.id
                val imageUrl = document.getString("imageUrl") ?: ""
                val providerId = document.getString("providerId") ?: ""
                val request = RequestModel(selectedOcupation, selectedServiceType, clientId, date, description, imageUrl, maxCost, providerId, requestBidAmount, title, state, requestId  )
                requestsList.add(request)
            }

            requestsList // Return the list of requests

        } catch (e: Exception) {
            // Handle errors
            e.printStackTrace()
            emptyList() // Return an empty list or handle the error as needed
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val userType = sharedPreferences.getString(
            "userType",
            ""
        )  // Retrieve the 'userType' attribute from SharedPreferences
        val userId = sharedPreferences.getString(
            "clientId",
            ""
        )  // Retrieve the 'clientId' attribute from SharedPreferences
        lifecycleScope.launch {

            val requests = getRequests()


            for(r in requests){
                val proposalsOfCurrentRequest = getProposalsByRequestId(r.id)
                if (userType == "CLIENT" && userId == r.clientId && r.providerId=="") {
                            requestList.add(toRequest(r))
                        } else if (userType == "PROVIDER" && !(proposalsOfCurrentRequest.any { it.providerId == userId })) {
                            //comparar si el userId no coincide con ninguna de las proposals asociadas a la request actual
                                requestList.add(toRequest(r))
                        }

            }
            requestListAdapter.notifyDataSetChanged()
        }
        //TODO filtrar solicitudes por id de cliente




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_requests_list, container, false)
        recRequestList = v.findViewById(R.id.rec_requestsList)
        filterContainer = v.findViewById(R.id.filterContainer)
        return v
    }

    override fun onStart() {
        super.onStart()


        recRequestList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recRequestList.layoutManager = linearLayoutManager
        requestListAdapter = RequestCardAdapter(requestList, this)
        recRequestList.adapter = requestListAdapter


        refreshRecyclerView()
    }

    fun refreshRecyclerView() {
        for (filterName in ocupationList) {
            val btnFilter = Button(context)
            btnFilter.text = filterName
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(10, 5, 10, 0)
            btnFilter.layoutParams = layoutParams
            btnFilter.textSize = 16F
            btnFilter.background = resources.getDrawable(R.drawable.rounded_button)

            btnFilter.setOnClickListener {
                val filter = btnFilter.text.toString()
                val filteredList =
                    requestList.filter { it.categoryOcupation == filter } as MutableList
                requestListAdapter = RequestCardAdapter(filteredList, this@RequestsListFragment)
                recRequestList.adapter = requestListAdapter
            }

            filterContainer.addView(btnFilter)
        }
    }

    override fun onViewItemDetail(request: Request) {
        val sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        // Retrieve the 'userType' attribute from SharedPreferences
        val userType = sharedPreferences.getString("userType", "")
        val actionForClient =
            RequestsListFragmentDirections.actionRequestsListFragmentToProviderRequestsFragment(
                request
            )
        val actionForProvider =
            RequestsListFragmentDirections.actionRequestsListFragmentToProposalFragment3(request)
        val navController = v.findNavController()

        if (userType == "CLIENT") {
            navController.navigate(actionForClient)
        } else {
            navController.navigate(actionForProvider)
        }
    }
    fun toRequest(r: RequestModel) : Request{
        return Request(r.requestTitle, r.requestBidAmount, r.categoryOcupation, r.categoryService, r.description, r.state, r.date, r.maxCost, r.clientId, r.id, r.imageUrl)
    }
    data class ProposalModel(
        val bid: Int,
        val commentary: String,
        val disabled: Boolean,
        val providerId: String,
        val requestId: String,
        val stability: Int
    ) {
        // Add a no-argument constructor
        constructor() : this(0, "", false, "", "", 0)
    }

    data class RequestModel(
        val categoryOcupation: String,
        val categoryService: String,
        val clientId: String,
        val date: String,
        val description: String,
        val imageUrl: String,
        val maxCost: Int,
        val providerId: String,
        val requestBidAmount: Int,
        val requestTitle: String,
        val state: String,
        val id: String
    ) {
        constructor() : this(
            "", "", "", "", "", "", 0, "", 0, "", "", ""
        )
    }

}