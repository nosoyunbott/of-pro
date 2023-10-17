package com.ar.of_pro.services

import com.google.firebase.firestore.FirebaseFirestore

class UserService {
    companion object{
        private val db = FirebaseFirestore.getInstance()
        private val usersCollection = db.collection("Users")

        fun updateRatingOfUser(ratingPoint:Float,userId:String)
        {
            val requestDoc = usersCollection.document(userId)
            val updates = hashMapOf<String,Any>(
                "rating" to ratingPoint
            )
            requestDoc.update(updates)
        }
    }
}