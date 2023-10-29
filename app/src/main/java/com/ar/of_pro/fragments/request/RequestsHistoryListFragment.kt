package com.ar.of_pro.fragments.request

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.ar.of_pro.entities.RequestHistory
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.ar.of_pro.utils.DateUtils
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RequestsHistoryListFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: HistoryCardAdapter
    var requestList: MutableList<RequestHistory> = ArrayList()

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    val usersCollection = db.collection("Users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        //usar un userId que
        val sharedPref = context?.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val userType=sharedPref!!.getString("userType","")
        val clientId=sharedPref!!.getString("clientId","")
        var userValue=""
        if(userType=="CLIENT")
        {
             userValue="clientId"
        }else
        {
             userValue="providerId"
        }


        requestsCollection.whereEqualTo("state", validStatesEnCurso).whereEqualTo(userValue,clientId)
            .get().addOnSuccessListener { documents ->

            for (document in documents) {

                    val title = document.getString("requestTitle") ?: ""
                    val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
                    val selectedOcupation = document.getString("categoryOcupation") ?: ""
                    val selectedServiceType = document.getString("categoryService") ?: ""
                    val description = document.getString("description") ?: ""
                    val state = document.getString("state") ?: ""
                    val dateTimestamp = document.getString("date") ?: ""
                    val date=DateUtils.GetFormattedDate(dateTimestamp)
                    val maxCost = document.getLong("maxCost")?.toInt() ?: 0
                    val clientId = document.getString("clientId") ?: ""
                    val providerId = document.getString("providerId") ?: ""
                    val requestId = document.id
                    val imageUrl = document.getString("imageUrl") ?: ""


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
                        requestId,
                        imageUrl
                    )

                    usersCollection.document(clientId).get()
                        .addOnSuccessListener { clientDocument ->
                            val clientName = clientDocument.getString("name") ?: ""

                            // Query the user collection to get the provider's name
                            usersCollection.document(providerId).get()
                                .addOnSuccessListener { providerDocument ->
                                    val providerName = providerDocument.getString("name")
                                        ?: "" + providerDocument.getString("lastName") ?: ""

                                    // Create a RequestHistory object and add it to the list
                                    val requestHistory = RequestHistory(r, clientName, providerName)
                                    requestList.add(requestHistory)
                                    requestListAdapter.notifyDataSetChanged()
                                    //FIN 'En estado'




                                }
                        }.addOnFailureListener { clientException ->
                            val requestHistory = RequestHistory(r, "clientName", "providerName")
                            requestList.add(requestHistory)
                        }




            }


        }.addOnFailureListener { Exception ->
            println("Error getting documents: $Exception")
            Log.e("ERROR DE DB", "$Exception")
        }

        requestsCollection.whereEqualTo("state", validStatesFinalizada).whereEqualTo(userValue,clientId)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {

                    val title = document.getString("requestTitle") ?: ""
                    val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
                    val selectedOcupation = document.getString("categoryOcupation") ?: ""
                    val selectedServiceType = document.getString("categoryService") ?: ""
                    val description = document.getString("description") ?: ""
                    val state = document.getString("state") ?: ""
                    val dateTimestamp = document.getString("date") ?: ""
                    val date=DateUtils.GetFormattedDate(dateTimestamp)
                    val maxCost = document.getLong("maxCost")?.toInt() ?: 0
                    val clientId = document.getString("clientId") ?: ""
                    val providerId = document.getString("providerId") ?: ""
                    val requestId = document.id
                    val imageUrl = document.getString("imageUrl") ?: ""


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
                        requestId,
                        imageUrl
                    )

                    usersCollection.document(clientId).get()
                        .addOnSuccessListener { clientDocument ->
                            val clientName = clientDocument.getString("name") ?: ""

                            // Query the user collection to get the provider's name
                            usersCollection.document(providerId).get()
                                .addOnSuccessListener { providerDocument ->
                                    val providerName = providerDocument.getString("name")
                                        ?: "" + providerDocument.getString("lastName") ?: ""

                                    // Create a RequestHistory object and add it to the list
                                    val requestHistory = RequestHistory(r, clientName, providerName)
                                    requestList.add(requestHistory)
                                    requestListAdapter.notifyDataSetChanged()
                                }}}


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
            RequestsHistoryListFragmentDirections.actionRequestsHistoryFragmentToRequestFragmentProccessFinishClient(
                request
            )
        )
    }


}


/*
*  //Esta va a ser la lista total ordenada
                                    requestList.add(requestHistory)
                                    requestListAdapter.notifyDataSetChanged()
* */