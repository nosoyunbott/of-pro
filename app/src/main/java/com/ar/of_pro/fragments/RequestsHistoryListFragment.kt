package com.ar.of_pro.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.RequestHistoryAdapter
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.RequestHistoryProvider
import com.ar.of_pro.entities.ServiceType

class RequestsHistoryListFragment:Fragment() {
    var recyclerView: RecyclerView?=null;
    lateinit var v : View




    var serviceTypesList : List<String> = ServiceType().getList()
    lateinit var serviceTypesAdapter: ArrayAdapter<String>
    lateinit var selectedServiceType : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_history_list, container, false)

        // Find the RecyclerView within the fragment's layout
        recyclerView = v.findViewById<RecyclerView>(R.id.rec_requestsHistoryList)
        recyclerView?.layoutManager=LinearLayoutManager(requireContext())
        recyclerView?.adapter=RequestHistoryAdapter(RequestHistoryProvider.RequestHistoryList)
        return v
    }



    override fun onStart() {

        super.onStart()
    }





    private fun showDialog(){
        val dialog = AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("ROMPISTE TODO")
            .setCancelable(true)
            .create()
        dialog.show()
    }


}