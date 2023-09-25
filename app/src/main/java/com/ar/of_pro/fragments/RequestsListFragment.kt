package com.ar.of_pro.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.RequestCardAdapter
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.Request

import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random


class RequestsListFragment : Fragment(), OnViewItemClickedListener {

    //Spinner setup
    lateinit var spnOcupation : Spinner
    var ocupationList : List<String> = Ocupation().getList()
    lateinit var ocupationAdapter: ArrayAdapter<String>
    lateinit var selectedOcupation : String

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
        spnOcupation = v.findViewById(R.id.spnOcupations2)
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

        setupSpinner(spnOcupation, ocupationAdapter)
        refreshRecycleView()
    }

    fun refreshRecycleView(){
        spnOcupation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (spnOcupation) {
                    spnOcupation -> selectedOcupation = ocupationList[position]
                }
                val filteredList = requestList.filter { it.category == selectedOcupation } as MutableList
                requestListAdapter = RequestCardAdapter(filteredList, this@RequestsListFragment)
                recRequestList.adapter = requestListAdapter
                filteredList.forEach {
                    Log.d("a", it.requestTitle)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showDialog()
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        ocupationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ocupationList)
    }
    
    override fun onViewItemDetail(request: Request) {
    //val action = RequestsListFragmentDirections.actionRequestsListFragmentToProposalFragment(request)
        //v.findNavController().navigate(action)
        val action = RequestsListFragmentDirections.actionRequestsListFragmentToProposalFragment(request)
        v.findNavController().navigate(action)
        Snackbar.make(v,request.requestTitle,Snackbar.LENGTH_SHORT).show()
    }

    private fun setupSpinner(spinner: Spinner, adapter: ArrayAdapter<String>) {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (spinner) {
                    spnOcupation -> selectedOcupation = ocupationList[position]
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showDialog()
            }
        }
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