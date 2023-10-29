package com.ar.of_pro.fragments.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.services.ActivityServiceApiBuilder
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class ProfileEditFragment : Fragment() {

    lateinit var v : View
    lateinit var btnAccept: Button
    lateinit var btnCancel: Button

    lateinit var txtNombre: EditText
    lateinit var txtTelefono: EditText
    lateinit var txtLocalidad: EditText
    lateinit var txtBio: EditText
    lateinit var txtSurname: EditText

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("Users")
    lateinit var sharedPreferences: SharedPreferences

    private val PICK_MEDIA_REQUEST = 1
    lateinit var profilePictureEdit: ImageView
    private var imageUrl = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        btnAccept = v.findViewById(R.id.btnAccept)
        btnCancel = v.findViewById(R.id.btnCancel)
        txtNombre = v.findViewById(R.id.txtNombre)
        txtTelefono = v.findViewById(R.id.txtTelefono)
        txtLocalidad = v.findViewById(R.id.txtLocalidad)
        txtBio = v.findViewById(R.id.txtDescripcion)
        txtSurname = v.findViewById(R.id.txtApellido)
        profilePictureEdit = v.findViewById(R.id.profilePictureEdit)
        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)

        return v
    }

    override fun onStart() {
        super.onStart()

        val filename = "myfile"
        val fileContents = "Hello world!"
        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }

        setOnClickListener(profilePictureEdit)

        btnAccept.setOnClickListener{

            //TODO actualizar todos los datos en la db
            if (imageUrl.isNullOrEmpty()) {
                updateDb(txtNombre.text.toString(), txtSurname.text.toString() ,txtTelefono.text.toString(), txtLocalidad.text.toString(), txtBio.text.toString())
            } else {
                updateDb(txtNombre.text.toString(), txtSurname.text.toString() ,txtTelefono.text.toString(), txtLocalidad.text.toString(), txtBio.text.toString(), imageUrl)
            }
            //    v.findNavController().popBackStack()
            val action = ProfileEditFragmentDirections.actionProfileEditFragmentToProfileFragment()
            v.findNavController().navigate(action)
        }

        btnCancel.setOnClickListener {
         //   v.findNavController().popBackStack()
            val action = ProfileEditFragmentDirections.actionProfileEditFragmentToProfileFragment()
            v.findNavController().navigate(action)
        }

    }

    fun updateDb(name: String?, apellido: String?, phone: String?, ubi: String?, description: String?) {

        val userDoc = usersCollection.document(sharedPreferences.getString("clientId", "").toString())

        //Name
        if (name!!.isNotEmpty()) {
            val name = hashMapOf<String, Any?>("name" to name) //TODO validar
            userDoc.update(name)
        }

        //lastName
        if (apellido!!.isNotEmpty()) {
            val lastName = hashMapOf<String, Any?>("lastName" to apellido) //TODO validar
            userDoc.update(lastName)
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

    fun updateDb(name: String?, apellido: String?, phone: String?, ubi: String?, description: String?, imgurImage: String?) {

        val userDoc = usersCollection.document(sharedPreferences.getString("clientId", "").toString())

        //Name
        if (name!!.isNotEmpty()) {
            val name = hashMapOf<String, Any?>("name" to name) //TODO validar
            userDoc.update(name)
        }

        //lastName
        if (apellido!!.isNotEmpty()) {
            val lastName = hashMapOf<String, Any?>("lastName" to apellido) //TODO validar
            userDoc.update(lastName)
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

        //Image
        if (imgurImage!!.isNotEmpty() && imageUrl!!.isNotEmpty()) {
            val imageToDb = hashMapOf<String, Any>("imageUrl" to imgurImage) //TODO validar
            userDoc.update(imageToDb)
        }
    }


    private fun setOnClickListener(img: ImageView) {
        img.setOnClickListener() {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*, video/*"
            startActivityForResult(intent, PICK_MEDIA_REQUEST)

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_MEDIA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedMediaUri = data.data
                val blob = uriToBlob(selectedMediaUri!!)

                if (selectedMediaUri != null) {
                    requireContext().openFileOutput("foto", Context.MODE_PRIVATE).use {
                        it.write(blob)
                    }
                } else {
                    Log.d("Error Foto", "MAL")
                }
                try {
                    loadImage(selectedMediaUri)
                    Toast.makeText(
                        context,
                        "Imagen subida correctamente.",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: FileNotFoundException) {
                    Toast.makeText(
                        context,
                        "Error con la imagen.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Exc", e.toString())
                }
            }
        }
    }

    fun uriToBlob(uri: Uri): ByteArray {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes() ?: byteArrayOf()
        inputStream?.close()
        return byteArray
    }

    fun loadImage(uri: Uri) {
        val file = File(requireContext().filesDir, "foto")
        try {

            val service = ActivityServiceApiBuilder.create()
            val requestBody = RequestBody.create(
                MediaType.parse(requireContext().contentResolver.getType(uri)),
                file
            )
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

            service.uploadImage(imagePart).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        try {
                            val responseData = response.body()?.string()
                            val jsonResponse = JSONObject(responseData)
                            val dataObject = jsonResponse.getJSONObject("data")
                            val imageUrll = dataObject.getString("link")
                            imageUrl = dataObject.getString("link")
                            Log.d("image", "Image URL: $imageUrll")
                            // Handle imageUrl as needed (e.g., display it in your app)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            // Handle JSON parsing error
                        }
                    } else {
                        // Handle unsuccessful response here
                        Log.e("image", "Upload failed")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle network errors or errors in the server
                    Log.e("image", "Error: " + t.message)
                }
            })

        } catch (e: IOException) {
            e.printStackTrace()
            // Handle file IO exception
        }
    }

}