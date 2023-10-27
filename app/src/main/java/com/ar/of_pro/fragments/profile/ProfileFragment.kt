package com.ar.of_pro.fragments.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    lateinit var v: View
    lateinit var btnEdit: Button
    lateinit var btnLogout: Button

    //Db
    val db = FirebaseFirestore.getInstance()
    val userRequest = db.collection("Users")
    val currentUser = FirebaseAuth.getInstance().currentUser
    val mail = currentUser?.email


    //Vars
    lateinit var txtNombre: TextView
    lateinit var txtLocalidad: TextView
    lateinit var txtCorreo: TextView
    lateinit var txtTelefono: TextView
    lateinit var txtNumRating: TextView
    lateinit var txtBioDescription: TextView
    lateinit var txtBio: TextView
    lateinit var txtRateQuantity2: TextView

    //Img
    lateinit var imageUrl: String
    lateinit var profilePicture: ImageView

    //Shared
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        btnEdit = v.findViewById(R.id.btnEdit)
        btnLogout = v.findViewById(R.id.btnLogout)
        txtNombre = v.findViewById(R.id.txtNombre)
        txtLocalidad = v.findViewById(R.id.txtLocalidad)
        txtCorreo = v.findViewById(R.id.txtCorreo)
        txtTelefono = v.findViewById(R.id.txtTelefono)
        txtNumRating = v.findViewById(R.id.numRating)
        txtRateQuantity2 = v.findViewById(R.id.txtRateQuantity2)
        profilePicture = v.findViewById(R.id.profilePicture)
        txtBioDescription =
            v.findViewById(R.id.txtBioDescription)
        txtBio = v.findViewById(R.id.txtBio)

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
                    val phoneNumber = snapshot.getLong("phone")?.toInt() ?: 0
                    val numRating = snapshot.getLong("rating")?.toInt() ?: 0
                    val profileImage = snapshot.getString("imageUrl") ?: ""

                    txtNombre.text = name + " " + surname
                    txtLocalidad.text = location
                    txtCorreo.text = mail
                    txtTelefono.text = phoneNumber.toString()
                    txtNumRating.text = numRating.toString() + " "

                    imageUrl = profileImage
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .into(profilePicture);

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

        btnLogout.setOnClickListener {

            Handler().postDelayed(

                {
                    Log.d("BEFORE LOGOUT", FirebaseAuth.getInstance().currentUser.toString())
                    FirebaseAuth.getInstance().signOut()
                    Log.d("AFTER LOGOUT", FirebaseAuth.getInstance().currentUser.toString())
                    val action = ProfileFragmentDirections.actionProfileFragmentToAuthActivity()
                    v.findNavController().navigate(action)
                }, 300
            )
        }

        btnEdit.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment()
            v.findNavController().navigate(action)
        }
    }


}