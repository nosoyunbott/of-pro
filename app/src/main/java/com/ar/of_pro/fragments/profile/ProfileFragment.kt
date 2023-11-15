package com.ar.of_pro.fragments.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.ar.of_pro.models.UserModel
import com.ar.of_pro.services.UserService
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
        txtBioDescription = v.findViewById(R.id.txtBioDescription)
        txtBio = v.findViewById(R.id.txtBio)
        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)

        return v
    }

    override fun onStart() {
        super.onStart()

        UserService.getUserById(
            sharedPreferences.getString(
                "clientId",
                ""
            ).toString()
        ) { document, exception ->
            if (exception == null && document != null) {
                val user = document.toObject(UserModel::class.java)
                if (user != null) {

                    val userTotalRating = String.format("%.1f", (user.rating / user.ratingQuantity))

                    txtNombre.text = "${user.name + " " + user.lastName}"
                    txtLocalidad.text = user.location
                    txtCorreo.text = user.mail
                    txtTelefono.text = user.phone.toString()

                    if (user.userType == "PROVIDER") {
                        txtBio.visibility = View.VISIBLE
                        txtBioDescription.text = user.bio
                        if (user.ratingQuantity > 0) {
                            txtNumRating.text =
                                "$userTotalRating 🌟"
                        } else {
                            txtNumRating.text = "$userTotalRating 🌟"

                            txtNumRating.text = ""
                            txtRateQuantity2.text = ""
                        }
                    }
                    Glide.with(requireContext()).load(user.imageUrl).into(profilePicture);
                }
            } else {
                Log.d("ErrorProfileEdit", "User not found")
            }
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val editor = sharedPreferences.edit()
            editor.remove("userType")
            editor.remove("clientId")
            editor.apply()
            val action = ProfileFragmentDirections.actionProfileFragmentToAuthActivity()
            v.findNavController().navigate(action)
        }

        btnEdit.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment()
            v.findNavController().navigate(action)
        }
    }

}