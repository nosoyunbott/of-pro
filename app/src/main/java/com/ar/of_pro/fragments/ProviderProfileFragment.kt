package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.google.firebase.firestore.FirebaseFirestore
import com.ar.of_pro.R.layout.fragment_provider_profile
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot


class ProviderProfileFragment : Fragment() {

    lateinit var v: View
    lateinit var btnEdit: Button


    val db = FirebaseFirestore.getInstance()
    val userRequest = db.collection("Users")
    val currentUser = FirebaseAuth.getInstance().currentUser
    val mail = currentUser?.email
    lateinit var txtNombre:TextView
    lateinit var txtLocalidad:TextView
    lateinit var txtCorreo:TextView
    lateinit var txtTelefono:TextView
    lateinit var numRating:TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_provider_profile, container, false)
        btnEdit = v.findViewById(R.id.btnEdit)
        txtNombre=v.findViewById(R.id.txtNombre)
        txtLocalidad=v.findViewById(R.id.txtLocalidad)
        txtCorreo =v.findViewById(R.id.txtCorreo)
        txtTelefono=v.findViewById(R.id.txtTelefono)
        numRating=v.findViewById(R.id.numRating)

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userRequest.get().addOnSuccessListener { snapshots ->
            for (snapshot in snapshots) {

                if (snapshot.getString("mail") == "client@test.com") {
                    val fullName = snapshot.getString("fullName") ?: ""
                    val location = snapshot.getString("location") ?: ""
                    val mail = snapshot.getString("mail") ?: ""
                    val phoneNumber = snapshot.getString("phoneNumber")?:""
                    val numRating = snapshot.hashCode()
                    txtNombre.text = fullName
                    txtLocalidad.text= location
                    txtCorreo.text=mail
                    txtTelefono.text=phoneNumber



                }
            }


        }


    }

    override fun onStart() {
        super.onStart()
        btnEdit.setOnClickListener {
            val action =
                ProviderProfileFragmentDirections.actionProviderProfileFragmentToProviderProfileEditFragment()
            v.findNavController().navigate(action)
        }
    }


}