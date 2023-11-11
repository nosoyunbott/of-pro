package com.ar.of_pro.fragments.request

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
import com.ar.of_pro.models.RequestModel
import com.ar.of_pro.services.ProposalsService
import com.ar.of_pro.services.RequestsService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RequestsListFragment : Fragment(), OnViewItemClickedListener {


    lateinit var filterContainer: LinearLayout
    var ocupationList: List<String> = Ocupation().getList()

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    var requestList: MutableList<Request> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: RequestCardAdapter

    val db = FirebaseFirestore.getInstance()
    private var selectedButton: Button? = null
    private lateinit var clearFiltersTextView: TextView

    lateinit var sharedPreferences : SharedPreferences
    lateinit var userType: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_requests_list, container, false)
        recRequestList = v.findViewById(R.id.rec_requestsList)
        filterContainer = v.findViewById(R.id.filterContainer)
        clearFiltersTextView = v.findViewById(R.id.clearFiltersTextView)

        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        userType = sharedPreferences.getString(
            "userType",
            ""
        )!!
        val userId = sharedPreferences.getString(
            "clientId",
            ""
        )
        requestList.clear()
        filterRequests(userId)
        recRequestList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recRequestList.layoutManager = linearLayoutManager
        requestListAdapter = RequestCardAdapter(requestList, this)
        recRequestList.adapter = requestListAdapter
        return v
    }

    private fun filterRequests(userId: String?) {
        lifecycleScope.launch {
            val requests = RequestsService.getRequests()
            for (r in requests) {
                val proposalsOfCurrentRequest = ProposalsService.getProposalsByRequestId(r.id)

                val PROVIDER_HAS_NOT_APPLIED =
                    userType == "PROVIDER" && !(proposalsOfCurrentRequest.any { it.providerId == userId }) && r.state == "PENDIENTE"
                val REQUEST_MISSING_PROIVDER =
                    userType == "CLIENT" && userId == r.clientId && r.providerId == ""

                if (REQUEST_MISSING_PROIVDER) {
                    requestList.add(toRequest(r))
                } else if (PROVIDER_HAS_NOT_APPLIED) {
                    requestList.add(toRequest(r))
                }

            }
            requestListAdapter.notifyDataSetChanged()
        }
    }

    override fun onStart() {
        super.onStart()



        refreshRecyclerByFilterButtons()
    }

    private fun refreshRecyclerByFilterButtons() {
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
            btnFilter.background = resources.getDrawable(R.drawable.button_transparent)

            btnFilter.setOnClickListener {
                val filter = btnFilter.text.toString()
                val filteredList =
                    requestList.filter { it.categoryOcupation == filter } as MutableList
                requestListAdapter = RequestCardAdapter(filteredList, this@RequestsListFragment)
                recRequestList.adapter = requestListAdapter

                selectedButton?.setBackgroundResource(R.drawable.button_transparent)
                btnFilter.setBackgroundResource(R.drawable.rounded_violet_background)
                selectedButton = btnFilter
            }

            filterContainer.addView(btnFilter)
        }
        clearFiltersTextView.setOnClickListener {
            selectedButton?.setBackgroundResource(R.drawable.button_transparent)
            requestListAdapter = RequestCardAdapter(requestList, this@RequestsListFragment)
            recRequestList.adapter = requestListAdapter
        }
    }

    override fun onViewItemDetail(request: Request) {
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

    private fun toRequest(r: RequestModel): Request {
        return Request(
            r.requestTitle,
            r.requestBidAmount,
            r.categoryOcupation,
            r.categoryService,
            r.description,
            r.state,
            r.date,
            r.maxCost,
            r.clientId,
            r.id,
            r.imageUrlArray as MutableList<String>
        )
    }


}