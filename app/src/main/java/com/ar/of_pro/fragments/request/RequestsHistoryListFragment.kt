package com.ar.of_pro.fragments.request

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.HistoryCardAdapter
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.RequestHistory
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.ar.of_pro.models.UserModel
import com.ar.of_pro.services.RequestsService
import com.ar.of_pro.services.UserService
import com.ar.of_pro.utils.DateUtils
import com.ar.of_pro.utils.RequestUtil
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException


class RequestsHistoryListFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    lateinit var recRequestList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestListAdapter: HistoryCardAdapter
    var requestList: MutableList<RequestHistory> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_history_list, container, false)

        recRequestList = v.findViewById(R.id.rec_requestsHistoryList)

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val validStatesEnCurso = "EN CURSO"
        val validStatesFinalizada = "FINALIZADA"

        //usar un userId que
        val sharedPref = context?.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val userType = sharedPref!!.getString("userType", "")
        val clientId = sharedPref!!.getString("clientId", "")
        var userValue = ""
        if (userType == "CLIENT") {
            userValue = "clientId"
        } else {
            userValue = "providerId"
        }
//TODO Extraer codigo adentro de lifecycleScope a funciones y encadenar EN CURSO con FINALIZADA
        lifecycleScope.launch {
            //For request "EN CURSO"
            val requestsInProgress = RequestsService.getRequestByState(validStatesEnCurso, userValue, clientId!!)

            for(r in requestsInProgress){
                var providerName = ""
                var clientName = ""
                UserService.getUserById(r.providerId){ document, exception ->
                    if (exception == null && document != null) {
                        val lastName = document.getString("lastName")
                        val name = document.getString("name")
                        Log.d("lastname", lastName!!)
                        Log.d("name", name!!)
                        providerName = name + " " + lastName
                    } else {
                        Log.d("ErrorProfileEdit", "User not found")
                    }
                    UserService.getUserById(r.clientId){ document, exception ->
                        if (exception == null && document != null) {
                            val lastName = document.getString("lastName")
                            val name = document.getString("name")
                            clientName = name + " " + lastName
                        } else {
                            Log.d("ErrorProfileEdit", "User not found")
                        }
                        val requestHistory = RequestHistory(RequestUtil.toRequest(r), clientName, providerName)
                        Log.d("adsdsaads", requestHistory.toString())
                        requestList.add(requestHistory)

                    }

                }
            }
            //For request "FINALIZADA"

            val requests = RequestsService.getRequestByState(validStatesFinalizada, userValue, clientId!!)
            for(r in requests){
                var providerName = ""
                var clientName = ""
                UserService.getUserById(r.providerId){ document, exception ->
                    if (exception == null && document != null) {
                        val lastName = document.getString("lastName")
                        val name = document.getString("name")
                        Log.d("lastname", lastName!!)
                        Log.d("name", name!!)
                        providerName = name + " " + lastName
                    } else {
                        Log.d("ErrorProfileEdit", "User not found")
                    }
                    UserService.getUserById(r.clientId){ document, exception ->
                        if (exception == null && document != null) {
                            val lastName = document.getString("lastName")
                            val name = document.getString("name")
                            clientName = name + " " + lastName
                        } else {
                            Log.d("ErrorProfileEdit", "User not found")
                        }
                        val requestHistory = RequestHistory(RequestUtil.toRequest(r), clientName, providerName)
                        Log.d("adsdsaads", requestHistory.toString())
                        requestList.add(requestHistory)
                        requestListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        recRequestList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recRequestList.layoutManager = linearLayoutManager
        requestListAdapter = HistoryCardAdapter(requestList, this)
        recRequestList.adapter = requestListAdapter
    }

    override fun onViewItemDetail(request: Request) {
        v.findNavController().navigate(
            RequestsHistoryListFragmentDirections.actionRequestsHistoryFragmentToRequestFragmentProccessFinishClient(
                request
            )
        )
    }
}