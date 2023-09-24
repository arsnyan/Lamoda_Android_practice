package com.arsnyan.lamodacopy

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {
    val TAG = "FIREBASE_REPOSITORY"
    var db = FirebaseFirestore.getInstance()

    fun getProducts(): CollectionReference {
        return db.collection("product")
    }
}