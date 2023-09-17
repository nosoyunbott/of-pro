package com.ar.of_pro.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.ar.of_pro.R
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.ServiceType

class RequestFragment : Fragment() {

    lateinit var v : View
    lateinit var spnOcupation : Spinner
    lateinit var spnServiceTypes : Spinner

    var ocupationList : List<String> = Ocupation().getList()
    lateinit var ocupationAdapter: ArrayAdapter<String>
    lateinit var selectedOcupation : String

    var serviceTypesList : List<String> = ServiceType().getList()
    lateinit var serviceTypesAdapter: ArrayAdapter<String>
    lateinit var selectedServiceType : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request, container, false)
        spnOcupation = v.findViewById(R.id.spnOcupations)
        spnServiceTypes = v.findViewById(R.id.spnServiceTypes)
        return v
    }

    override fun onStart() {
        super.onStart()

        setupSpinner(spnOcupation, ocupationAdapter)
        setupSpinner(spnServiceTypes, serviceTypesAdapter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        ocupationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ocupationList)
        serviceTypesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, serviceTypesList)
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
                    spnServiceTypes -> selectedServiceType = serviceTypesList[position]
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