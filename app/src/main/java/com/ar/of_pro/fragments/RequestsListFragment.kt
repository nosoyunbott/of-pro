package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.RequestCardAdapter
import com.ar.of_pro.entities.Request
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.google.android.material.snackbar.Snackbar


class RequestsListFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    var requestList: MutableList<Request> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: RequestCardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_requests_list, container, false)
        recRequestList = v.findViewById(R.id.rec_requestsList)
        return v
    }

    override fun onStart() {
        super.onStart()
       for (i in 1..10) {
           requestList.add(Request("Pintar paredes en una cocina", 9))
           requestList.add(Request("Arreglar canilla que pierde", 0))
           requestList.add(Request("Instalar aire acondicionado", 23))
       }

        recRequestList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recRequestList.layoutManager = linearLayoutManager
        requestListAdapter = RequestCardAdapter(requestList, this)
        recRequestList.adapter = requestListAdapter

    }

    override fun onViewItemDetail(request: Request) {
    val action = RequestsListFragmentDirections.actionRequestsListFragmentToProposalFragment(request)
        v.findNavController().navigate(action)

        Snackbar.make(v,request.requestTitle,Snackbar.LENGTH_SHORT).show()
    }


}