package com.ar.of_pro.fragments.profile

import android.content.Context
import android.content.SharedPreferences
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
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    lateinit var v: View
    lateinit var btnEdit: Button


    val db = FirebaseFirestore.getInstance()
    val userRequest = db.collection("Users")
    val currentUser = FirebaseAuth.getInstance().currentUser
    val mail = currentUser?.email
    lateinit var txtNombre: TextView
    lateinit var txtLocalidad: TextView
    lateinit var txtCorreo: TextView
    lateinit var txtTelefono: TextView
    lateinit var txtNumRating: TextView
    lateinit var txtBioDescription: TextView
    lateinit var txtBio: TextView
    lateinit var txtRateQuantity2: TextView
    lateinit var sharedPreferences: SharedPreferences
    //lateinit var txtServiceType: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        btnEdit = v.findViewById(R.id.btnEdit)
        txtNombre = v.findViewById(R.id.txtNombre)
        txtLocalidad = v.findViewById(R.id.txtLocalidad)
        txtCorreo = v.findViewById(R.id.txtCorreo)
        txtTelefono = v.findViewById(R.id.txtTelefono)
        txtNumRating = v.findViewById(R.id.numRating)
        txtRateQuantity2 = v.findViewById(R.id.txtRateQuantity2)
        txtBioDescription =
            v.findViewById(R.id.txtBioDescription) //TODO traer txt de descripcion y mostrar si el userType es provider
        txtBio = v.findViewById(R.id.txtBio)
        //txtServiceType = v.findViewById(R.id.txtServiceType) //TODO traer txt de rubro y mostrar si el userType es provider ? dinamico

        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userRequest.get().addOnSuccessListener { snapshots ->
            for (snapshot in snapshots) {

                //db.collection('books').where(firebase.firestore.FieldPath.documentId(), '==', 'fK3ddutEpD2qQqRMXNW5').get()


                if (snapshot.getString("mail") == mail) {
                    val name = snapshot.getString("name") ?: ""
                    val surname = snapshot.getString("lastName") ?: ""
                    val location = snapshot.getString("location") ?: ""
                    val mail = snapshot.getString("mail") ?: ""
                    val phoneNumber = snapshot.getString("phoneNumber") ?: ""
                    val numRating = snapshot.getLong("rating")?.toInt() ?: 0
                    txtNombre.text = name + " " + surname
                    txtLocalidad.text = location
                    txtCorreo.text = mail
                    txtTelefono.text = phoneNumber
                    txtNumRating.text = numRating.toString() + " "
                    if (snapshot.getString("userType") == "PROVIDER") {
                        txtBioDescription.text = snapshot.getString("bio")
                    } else {
                        txtBio.text = ""
                        txtBioDescription.text = ""
                        txtNumRating.text = ""
                        txtRateQuantity2.text = ""

                    }

                }
            }


        }


    }

    override fun onStart() {
        super.onStart()
        btnEdit.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment()
            v.findNavController().navigate(action)
        }
    }


}