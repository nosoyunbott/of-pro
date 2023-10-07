package com.ar.of_pro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.RequestCardAdapter
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.Request
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class RequestListProviderFragment : Fragment(), OnViewItemClickedListener {


    lateinit var filterContainer: LinearLayout
    var ocupationList: List<String> = Ocupation().getList()

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    var requestList: MutableList<Request> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: RequestCardAdapter

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_list_provider, container, false)
        recRequestList = v.findViewById(R.id.rec_requestsList)
        filterContainer = v.findViewById(R.id.filterContainer)
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestsCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val title = document.getString("requestTitle") ?: ""
                val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
                val selectedOcupation = document.getString("categoryOcupation") ?: ""
                val selectedServiceType = document.getString("categoryService") ?: ""
                val description = document.getString("description") ?: ""
                val state = document.getString("state") ?: ""
                val date = document.getString("date") ?: ""
                val maxCost = document.getLong("maxCost")?.toInt() ?: 0
                val idClient = document.getString("idClient") ?: ""

                val r = Request(
                    title,
                    requestBidAmount,
                    selectedOcupation,
                    selectedServiceType,
                    description,
                    state,
                    date,
                    maxCost,
                    idClient
                )

                requestList.add(r)

            }

            requestListAdapter.notifyDataSetChanged()
        }
            .addOnFailureListener { Exception ->
                println("Error getting documents: $Exception")
            }

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

            // botones del carrousel de rubros
            btnFilter.setOnClickListener {
                val filter = btnFilter.text.toString()
                val filteredList =
                    requestList.filter { it.categoryOcupation == filter } as MutableList
                requestListAdapter =
                    RequestCardAdapter(filteredList, this@RequestListProviderFragment)
                recRequestList.adapter = requestListAdapter
            }

            filterContainer.addView(btnFilter)
        }
    }

    override fun onViewItemDetail(request: Request) {
        //for service provider
        val action =
            RequestListProviderFragmentDirections.actionRequestListProviderFragmentToProposalFragment(
                request
            )
        v.findNavController().navigate(action)
        Snackbar.make(v, request.requestTitle, Snackbar.LENGTH_SHORT).show()


    }
}