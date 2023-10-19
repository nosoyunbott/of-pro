package com.ar.of_pro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.HistoryCardAdapter
import com.ar.of_pro.entities.Request
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class RequestsHistoryListFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: HistoryCardAdapter
    var requestList: MutableList<Request> = ArrayList()

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_history_list, container, false)

        recRequestList = v.findViewById(R.id.rec_requestsHistoryList)

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val validStatesEnCurso = "EN CURSO"
        val validStatesFinalizada = "FINALIZADA"

        val enCursoRequests = mutableListOf<Request>()
        val finalizadaRequests = mutableListOf<Request>()

// Query for "EN CURSO" requests
        requestsCollection
            .whereEqualTo("state", validStatesEnCurso)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Parse and add "EN CURSO" requests to the enCursoRequests list
                    val r = parseRequestFromDocument(document)
                    enCursoRequests.add(r)
                }

                // Query for "FINALIZADA" requests
                requestsCollection
                    .whereEqualTo("state", validStatesFinalizada)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            // Parse and add "FINALIZADA" requests to the finalizadaRequests list
                            val r = parseRequestFromDocument(document)
                            finalizadaRequests.add(r)
                        }

                        // Combine the results into a single list
                        val combinedRequests = enCursoRequests + finalizadaRequests

                        // Sort the combinedRequests by date in descending order
                        combinedRequests.sortedByDescending { it.date }

                        // Update your requestList with the combined and sorted requests
                        requestList.addAll(combinedRequests)
                        requestListAdapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        println("Error getting FINALIZADA documents: $exception")
                    }
            }
            .addOnFailureListener { exception ->
                println("Error getting EN CURSO documents: $exception")
            }
    }

    // Helper function to parse a Request object from a Firestore document
    private fun parseRequestFromDocument(document: DocumentSnapshot): Request {
        val title = document.getString("requestTitle") ?: ""
        val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
        val selectedOcupation = document.getString("categoryOcupation") ?: ""
        val selectedServiceType = document.getString("categoryService") ?: ""
        val description = document.getString("description") ?: ""
        val state = document.getString("state") ?: ""
        val date = document.getLong("date") ?: 0
        val maxCost = document.getLong("maxCost")?.toInt() ?: 0
        val clientId = document.getString("clientId") ?: ""
        return Request(
            title,
            requestBidAmount,
            selectedOcupation,
            selectedServiceType,
            description,
            state,
            date,
            maxCost,
            clientId,
            ""
        )
    }

    override fun onStart() {
        super.onStart()

        recRequestList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recRequestList.layoutManager = linearLayoutManager
        requestListAdapter = HistoryCardAdapter(requestList, this)
        recRequestList.adapter = requestListAdapter
    }

    override fun onViewItemDetail(request: Request) {

        v.findNavController().navigate(
            RequestsHistoryListFragmentDirections.actionRequestsHistoryFragmentToRequestFragmentProccessFinishProvider(
                request
            )
        )

        //TODO segun el tipo de cliente ir hacia pantalla de finalizacion de cliente o de proveedor (la unica q esta hecha es la de proveedor) @Moragues
    }


}