package com.ar.of_pro.fragments.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.entities.User
import com.ar.of_pro.entities.UserType
import com.ar.of_pro.services.ActivityServiceApiBuilder
import com.google.firebase.auth.FirebaseAuth
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

@Suppress("DEPRECATION")
class SignupFragment : Fragment() {

    lateinit var v: View
    lateinit var nameEdt: EditText
    lateinit var lastNameEdt: EditText
    lateinit var addressEdt: EditText
    lateinit var locationEdt: EditText
    lateinit var emailEdt: EditText
    lateinit var phoneEdt: EditText

    private val PICK_MEDIA_REQUEST = 1
    lateinit var btnPhotos: Button
    lateinit var imageUrl: String

    lateinit var spnUserType: Spinner
    var userTypeList: List<String> = UserType().getList()
    lateinit var userTypeAdapter: ArrayAdapter<String>
    lateinit var selectedUserType: String

    lateinit var passwordEdt: EditText
    lateinit var registerButton: Button
    lateinit var logInTextView: TextView

    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_signup, container, false)
        nameEdt = v.findViewById(R.id.nameEdt)
        lastNameEdt = v.findViewById(R.id.lastNameEdt)
        addressEdt = v.findViewById(R.id.addressEdt)
        locationEdt = v.findViewById(R.id.locationEdt)
        emailEdt = v.findViewById(R.id.emailEdt)
        phoneEdt = v.findViewById(R.id.phoneEdt)
        spnUserType = v.findViewById(R.id.spnUserType)
        passwordEdt = v.findViewById(R.id.passwordEdt)
        registerButton = v.findViewById(R.id.registerButton)
        logInTextView = v.findViewById(R.id.logInTextView)
        btnPhotos = v.findViewById(R.id.btnPhotos)
        return v
    }

    override fun onStart() {
        super.onStart()

        val filename = "myfile"
        val fileContents = "Hello world!"
        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }

        setupSpinner(spnUserType, userTypeAdapter)
        signUp()
        goToLogInByTextView()
        setOnClickListener(btnPhotos)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        userTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userTypeList)
    }

    private fun setOnClickListener(btn: Button) {
        btn.setOnClickListener() {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*, video/*"
            startActivityForResult(intent, PICK_MEDIA_REQUEST)

        }
    }

    private fun signUp() {
        registerButton.setOnClickListener {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                emailEdt.text.toString(),
                passwordEdt.text.toString()
            ).addOnCompleteListener {
                val user = User(
                    nameEdt.text.toString(),
                    lastNameEdt.text.toString(),
                    addressEdt.text.toString(),
                    locationEdt.text.toString(),
                    emailEdt.text.toString(),
                    passwordEdt.text.toString(),
                    phoneEdt.text.toString().toInt(),
                    0.0,
                    0,
                    selectedUserType,
                    "Bio del usuario.",
                    imageUrl
                )

                val newDocUser = db.collection("Users").document()
                db.collection("Users").document(newDocUser.id).set(user)

                if (it.isSuccessful) {
                    val action =
                        SignupFragmentDirections.actionSignupFragmentToUserLoginFragment()
                    v.findNavController().navigate(action)
                }

            }

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
                    loadImage(selectedMediaUri, blob)
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

    fun loadImage(uri: Uri, blob: ByteArray) {
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
                    spnUserType -> selectedUserType = userTypeList[position]
                }
                if (selectedUserType == "CLIENTE") {
                    selectedUserType = "CLIENT"
                } else if (selectedUserType == "PROVEEDOR") {
                    selectedUserType = "PROVIDER"
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

    private fun goToLogInByTextView() {
        val spannableString = SpannableString(logInTextView.text)

        val startIndex = logInTextView.text.indexOf("Inicia Sesión")
        val endIndex = startIndex + "Inicia Sesión".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action =
                    SignupFragmentDirections.actionSignupFragmentToUserLoginFragment()
                v.findNavController().navigate(action)
            }
        }
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        logInTextView.text = spannableString
        logInTextView.movementMethod = LinkMovementMethod.getInstance()
    }

}