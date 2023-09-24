package com.arsnyan.lamodacopy.ui.productview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arsnyan.lamodacopy.FirestoreRepository
import com.arsnyan.lamodacopy.data.Product
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

class ProductScreenViewModel : ViewModel() {
    private val TAG = "PRODUCT_SCREEN_VM"
    private var firebaseRepository = FirestoreRepository()
    private var product: MutableLiveData<Product?> = MutableLiveData()
    fun getProduct(): MutableLiveData<Product?> {
        firebaseRepository.getProducts().addSnapshotListener(EventListener { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed", e)
                product.value = null
                return@EventListener
            }

            var prod: Product? = null
            for (p in value!!) {
                prod = p.toObject(Product::class.java)
            }
            product.value = prod
        })
        return product
    }
}