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
import com.google.firebase.firestore.FirebaseFirestore

class RequestsHistoryListFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: HistoryCardAdapter
    var requestList: MutableList<RequestHistory> = ArrayList()

    val db = FirebaseFirestore.getInstance()
    val requestsCollection = db.collection("Requests")
    val usersCollection=db.collection("Users")

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
                if (document.getString("state") == "EN CURSO" || document.getString("state") == "FINALIZADA") {
                    val title = document.getString("requestTitle") ?: ""
                    val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
                    val selectedOcupation = document.getString("categoryOcupation") ?: ""
                    val selectedServiceType = document.getString("categoryService") ?: ""
                    val description = document.getString("description") ?: ""
                    val state = document.getString("state") ?: ""
                    val date = document.getString("date") ?: ""
                    val maxCost = document.getLong("maxCost")?.toInt() ?: 0
                    val clientId = document.getString("clientId") ?: ""
                    val providerId=document.getString("providerId")?:""
                    val requestId=document.id
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

                    usersCollection.document(clientId)
                        .get()
                        .addOnSuccessListener { clientDocument ->
                            val clientName = clientDocument.getString("name")?:""

                            // Query the user collection to get the provider's name
                            usersCollection.document(providerId)
                                .get()
                                .addOnSuccessListener { providerDocument ->
                                    val providerName = providerDocument.getString("name")?:"" + providerDocument.getString("lastName")?:""

                                    // Create a RequestHistory object and add it to the list
                                    val requestHistory = RequestHistory(r, clientName, providerName)
                                    requestList.add(requestHistory)
                                    requestListAdapter.notifyDataSetChanged()
                                }
                                .addOnFailureListener { providerException ->


                                }
                        }
                        .addOnFailureListener { clientException ->
                            val requestHistory = RequestHistory(r, "clientName", "providerName")
                            requestList.add(requestHistory)
                        }




                }

            }



        }
            .addOnFailureListener { Exception ->
                println("Error getting documents: $Exception")
                Log.e("ERROR DE DB","$Exception")
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
        val sharedPreferences = requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val userType = sharedPreferences.getString("userType", "")  // Retrieve the 'userType' attribute from SharedPreferences

        if(userType == "PROVIDER") {
            v.findNavController().navigate(
                RequestsHistoryListFragmentDirections.actionRequestsHistoryFragmentToRequestFragmentProccessFinishProvider(
                    request
                )
            )
        } else if (userType == "CLIENT") {
            v.findNavController().navigate(
                RequestsHistoryListFragmentDirections.actionRequestsHistoryFragmentToRequestFragmentProccessFinishClient(
                    request
                )
            )
        } else {
            // Handle the case where 'userType' is neither 'provider' nor 'client'
            // You can provide a default action or show an error message.
        }


    }


}