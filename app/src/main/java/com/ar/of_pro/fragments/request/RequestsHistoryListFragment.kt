package com.ar.of_pro.fragments.request

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

        requestsCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.getString("state") == "EN CURSO") {
                    val title = document.getString("requestTitle") ?: ""
                    val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
                    val selectedOcupation = document.getString("categoryOcupation") ?: ""
                    val selectedServiceType = document.getString("categoryService") ?: ""
                    val description = document.getString("description") ?: ""
                    val state = document.getString("state") ?: ""
                    val date = document.getString("date") ?: ""
                    val maxCost = document.getLong("maxCost")?.toInt() ?: 0
                    val clientId = document.getString("clientId") ?: ""

                    val r = Request(
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

                    requestList.add(r)
                }
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
        requestListAdapter = HistoryCardAdapter(requestList, this)
        recRequestList.adapter = requestListAdapter
    }

    override fun onViewItemDetail(request: Request) {

        v.findNavController().navigate(
            RequestsHistoryListFragmentDirections.actionRequestsHistoryFragmentToRequestFragmentProccessFinishProvider(
                request
            )
        )
    }


}