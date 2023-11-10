package com.ar.of_pro.services

import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.RequestFB
import com.ar.of_pro.models.RequestModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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

         fun updateProposalsQtyFromId(requestId: String, proposalsQty: Int){
             val requestDoc = requestsCollection.document(requestId)

             val updates = hashMapOf<String, Any>(
                 "requestBidAmount" to proposalsQty + 1
             )
             requestDoc.update(updates)
         }

         fun updateRequestBidAmount(requestId: String) {
             val requestDoc = requestsCollection.document(requestId)

             val updates = hashMapOf<String, Any>(
                 "requestBidAmount" to FieldValue.increment(-1)
             )
             requestDoc.update(updates)
         }
         fun updateAll(requestId: String, request: RequestFB) {
             val requestDoc = requestsCollection.document(requestId)

             val updates = hashMapOf<String, Any>(
                 "categoryOcupation" to request.categoryOcupation,
                 "categoryService" to request.categoryService,
                 "clientId" to request.clientId,
                 "date" to request.date,
                 "description" to request.description,
                 "imageUrlArray" to request.imageUrlArray,
                 "maxCost" to request.maxCost,
                 "requestBidAmount" to request.requestBidAmount,
                 "requestTitle" to request.requestTitle
             )
             requestDoc.update(updates)
         }
         fun updateSingleStringField(fieldName: String, requestId: String, value: String){
             val requestDoc = requestsCollection.document(requestId)

             val updates = hashMapOf<String, Any>(
                 fieldName to value
             )
             requestDoc.update(updates)
         }
         fun deleteRequestById(requestId: String) {
             val requestsCollection = db.collection("Requests")
             val requestDoc = requestsCollection.document(requestId)

             requestDoc.delete()
         }
         suspend fun getRequests(): List<RequestModel> {
             return try {
                 val querySnapshot = requestsCollection.get().await()

                 val requestsList = mutableListOf<RequestModel>()


                 for (document in querySnapshot) {
                     val title = document.getString("requestTitle") ?: ""
                     val requestBidAmount = document.getLong("requestBidAmount")?.toInt() ?: 0
                     val selectedOcupation = document.getString("categoryOcupation") ?: ""
                     val selectedServiceType = document.getString("categoryService") ?: ""
                     val description = document.getString("description") ?: ""
                     val state = document.getString("state") ?: ""
                     val date = document.getString("date") ?: ""
                     val maxCost = document.getLong("maxCost")?.toInt() ?: 0
                     val clientId = document.getString("clientId") ?: ""
                     val requestId = document.id
                     val imageUrlArray =
                         document.get("imageUrlArray") as? MutableList<String> ?: mutableListOf()
                     val providerId = document.getString("providerId") ?: ""
                     val request = RequestModel(
                         selectedOcupation,
                         selectedServiceType,
                         clientId,
                         date,
                         description,
                         imageUrlArray,
                         maxCost,
                         providerId,
                         requestBidAmount,
                         title,
                         state,
                         requestId
                     )
                     requestsList.add(request)
                 }

                 requestsList

             } catch (e: Exception) {
                 e.printStackTrace()
                 emptyList()
             }
         }

         fun getRequestById(id: String, onSuccess: (DocumentSnapshot?) -> Unit, onFailure: (Exception) -> Unit) {
             requestsCollection.document(id)
                 .get()
                 .addOnSuccessListener { documentSnapshot ->
                     onSuccess(documentSnapshot)
                 }
                 .addOnFailureListener { exception ->
                     onFailure(exception)
                 }
         }
     }

    }
