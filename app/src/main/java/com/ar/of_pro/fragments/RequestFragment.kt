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

class RequestFragment : Fragment() {

    lateinit var v : View
    lateinit var spnOcupation : Spinner

    var ocupationList : List<String> = Ocupation().getList()
    lateinit var ocupationAdapter: ArrayAdapter<String>
    lateinit var selectedOcupation : String


    lateinit var serviceTypesAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request, container, false)
        spnOcupation = v.findViewById(R.id.spnOcupations)
        return v
    }

    override fun onStart() {
        super.onStart()

        setSpinners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        ocupationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ocupationList)
    }

    private fun setSpinners() {
        spnOcupation.adapter = ocupationAdapter
        spnOcupation.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedOcupation = ocupationList[position]
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