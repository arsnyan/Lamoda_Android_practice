package com.arsnyan.lamodacopy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    lateinit var firebaseAuth: FirebaseAuth
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: Flow<FirebaseUser?> = _user

    fun setUser(user: FirebaseUser?) {
        _user.value = user
    }
}