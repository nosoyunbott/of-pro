package com.ar.of_pro.fragments

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
import com.google.android.material.snackbar.Snackbar


class RequestsListFragment : Fragment(), OnViewItemClickedListener {


    lateinit var filterContainer: LinearLayout
    var ocupationList: List<String> = Ocupation().getList()

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    var requestList: MutableList<Request> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: RequestCardAdapter
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
        for (i in 1..10) {
            requestList.add(Request("Pintar paredes en una cocina", 9, ocupationList[1]))
            requestList.add(Request("Arreglar canilla que pierde", 0, ocupationList[0]))
            requestList.add(Request("Instalar aire acondicionado", 23, ocupationList[4]))
        }

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
                val filteredList = requestList.filter { it.category == filter } as MutableList
                requestListAdapter = RequestCardAdapter(filteredList, this@RequestsListFragment)
                recRequestList.adapter = requestListAdapter
            }

            filterContainer.addView(btnFilter)
        }
    }

    override fun onViewItemDetail(request: Request) {
        val action =
            RequestsListFragmentDirections.actionRequestsListFragmentToProposalFragment(request)
        v.findNavController().navigate(action)
        Snackbar.make(v, request.requestTitle, Snackbar.LENGTH_SHORT).show()
    }
}