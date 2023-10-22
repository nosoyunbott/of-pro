package com.ar.of_pro.fragments.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.entities.Request
import com.ar.of_pro.services.RequestsService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileEditFragment : Fragment() {

    lateinit var v : View
    lateinit var btnAccept: Button
    lateinit var txtNombre: EditText
    lateinit var txtTelefono: EditText
    lateinit var txtLocalidad: EditText
    lateinit var txtBio: EditText

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("Users")
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        btnAccept = v.findViewById(R.id.btnAccept)
        txtNombre = v.findViewById(R.id.txtNombre)
        txtTelefono = v.findViewById(R.id.txtTelefono)
        txtLocalidad = v.findViewById(R.id.txtLocalidad)
        txtBio = v.findViewById(R.id.txtDescripcion)

        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)

        return v
    }

    override fun onStart() {
        super.onStart()
        btnAccept.setOnClickListener{

            //TODO actualizar todos los datos en la db
            updateName(txtNombre.text.toString(), txtTelefono.text.toString(), txtLocalidad.text.toString(), txtBio.text.toString())

            val action = ProfileEditFragmentDirections.actionProfileEditFragmentToProfileFragment()
            v.findNavController().navigate(action)
        }

    }

    fun updateName(name: String?, phone: String?, ubi: String?, description: String?) {

        val userDoc = usersCollection.document(sharedPreferences.getString("clientId", "").toString())

        //Name
        if (name!!.isNotEmpty()) {
            val name = hashMapOf<String, Any?>("name" to name) //TODO validar
            userDoc.update(name)
        }

        //Phone
        if (phone!!.isNotEmpty()) {
            val phone = hashMapOf<String, Any?>("phone" to phone.toString().toInt()) //TODO validar
            userDoc.update(phone)
        }

        //Localidad

        if (ubi!!.isNotEmpty()) {
            val localidad = hashMapOf<String, Any?>("location" to ubi) //TODO validar
            userDoc.update(localidad)
        }

        //Bio
        if (description!!.isNotEmpty()) {
            val bio = hashMapOf<String, Any>("bio" to description) //TODO validar
            userDoc.update(bio)
        }
    }

}