package com.ar.of_pro.services

import com.ar.of_pro.entities.Request
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

 class RequestsService {


     companion object{
         private val db = FirebaseFirestore.getInstance()
         private val requestsCollection = db.collection("Requests")
         fun updateProviderIdFromRequest(requestId: String, providerId: String){
             val requestDoc = requestsCollection.document(requestId)
             val updates = hashMapOf<String, Any>(
                 "providerId" to providerId
             )
             requestDoc.update(updates)
             updateRequestState(Request.IN_COURSE, requestId)

         }

         fun updateRequestState(state: String, requestId: String) {
             val requestDoc = requestsCollection.document(requestId)
             val updates = hashMapOf<String, Any>(
                 "state" to state
             )
             requestDoc.update(updates)
         }
     }

    }
