package com.ar.of_pro.services

import android.util.Log
import com.ar.of_pro.entities.Proposal
import com.ar.of_pro.models.ProposalModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ProposalsService {
    companion object{
        private val db = FirebaseFirestore.getInstance()
        private val proposalsCollection = db.collection("Proposals")
        fun deleteProposalsFromRequestId(requestId: String) {
            val query = proposalsCollection.whereEqualTo("requestId", requestId)

            query.get().addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val result = querySnapshot.result
                    for(d in result.documents){
                        d.reference.delete()
                    }
                } else {
                    Log.d("ProposalsRetrievalError", "Error retrieving proposals: ${querySnapshot.exception}")
                }
            }
        }
        fun deleteProposalFromId(proposalId: String) {
            val proposalReference = proposalsCollection.document(proposalId)
            proposalReference.delete()
                .addOnSuccessListener {
                    Log.d("Delete proposal","DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.d("Delete proposal", "Error deleting document: $e")
                }
        }
        suspend fun getProposalsByRequestId(requestId: String): List<ProposalModel> {
            return try {
                val querySnapshot = proposalsCollection.get().await()

                val proposalsList = mutableListOf<ProposalModel>()

                for (document in querySnapshot) {
                    val proposal = document.toObject(ProposalModel::class.java)
                    if (proposal.requestId == requestId) {
                        proposalsList.add(proposal)
                    }
                }

                return proposalsList

            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
        fun getProposalByRequestIdAndProviderId(requestId: String, providerId: String, callback: (QuerySnapshot?, Exception?) -> Unit) {
            val query = proposalsCollection
                .whereEqualTo("requestId", requestId).whereEqualTo("providerId", providerId)
            query.get().addOnCompleteListener { querySnapshot ->
                    if (querySnapshot.isSuccessful) {
                        val result = querySnapshot.result
                        callback(result, null)
                    } else {
                        callback(null, querySnapshot.exception)
                    }
                }
        }

    }
}