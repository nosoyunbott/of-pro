package com.ar.of_pro.fragments

import android.app.Activity
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
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ar.of_pro.R
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.ServiceType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class RequestFragment : Fragment() {

    lateinit var v: View
    lateinit var spnOcupation: Spinner
    lateinit var spnServiceTypes: Spinner
    lateinit var btnAttach: Button
    lateinit var btnRequest: Button
    private val PICK_MEDIA_REQUEST = 1

    lateinit var edtTitle: EditText
    lateinit var edtPriceMax: EditText
    lateinit var edtDescripcion: EditText
    lateinit var edtTime: EditText

    var ocupationList: List<String> = Ocupation().getList()
    lateinit var ocupationAdapter: ArrayAdapter<String>
    lateinit var selectedOcupation: String

    var serviceTypesList: List<String> = ServiceType().getList()
    lateinit var serviceTypesAdapter: ArrayAdapter<String>
    lateinit var selectedServiceType: String

    private val db = FirebaseFirestore.getInstance()
    private var userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request, container, false)
        spnOcupation = v.findViewById(R.id.spnOcupations)
        spnServiceTypes = v.findViewById(R.id.spnServiceTypes)
        btnAttach = v.findViewById(R.id.btnAttach)
        btnRequest = v.findViewById(R.id.btnRequest)
        edtTitle = v.findViewById(R.id.edtTitle)
        edtPriceMax = v.findViewById(R.id.edtPriceMax)
        edtDescripcion = v.findViewById(R.id.edtDescripcion)
        edtTime = v.findViewById(R.id.edtTime)
        return v
    }

    override fun onStart() {
        super.onStart()

        setupSpinner(spnOcupation, ocupationAdapter)
        setupSpinner(spnServiceTypes, serviceTypesAdapter)
        setOnClickListener(btnAttach)

        btnRequest.setOnClickListener {
            val title = edtTitle.text.toString()
            val clientId = "1"

            val r = Request(
                title,
                0,
                selectedOcupation,
                selectedServiceType,
                edtDescripcion.text.toString(),
                Request.PENDING,
                edtTime.text.toString(),
                edtPriceMax.text.toString().toIntOrNull(),
                clientId,
                ""
            )

            val newDocRequest = db.collection("Requests").document()
            db.collection("Requests").document(newDocRequest.id).set(r)

            Toast.makeText(
                context,
                "state: ${Request.FINISHED}, Precio máximo: ${r.maxCost}",
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        ocupationAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ocupationList)
        serviceTypesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, serviceTypesList)
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

    private fun showDialog() {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("ROMPISTE TODO")
            .setCancelable(true)
            .create()
        dialog.show()
    }

    private fun setOnClickListener(btn: Button) {
        btn.setOnClickListener() {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*, video/*"
            startActivityForResult(intent, PICK_MEDIA_REQUEST)


        }
    }
}