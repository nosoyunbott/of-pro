package com.ar.of_pro.fragments.request

import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO filtrar solicitudes por id de cliente
        requestsCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                if(document.getString("providerId") == "") {
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
                        requestId
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

        val action2 =
            RequestsListFragmentDirections.actionRequestsListFragmentToProviderRequestsFragment(
                request
            )
        val navController = v.findNavController()
        navController.navigate(action2)

    }
}