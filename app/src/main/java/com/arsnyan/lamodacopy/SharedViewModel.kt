package com.arsnyan.lamodacopy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user

    fun setUser(user: FirebaseUser?) {
        _user.value = user
    }

    fun getUser(): FirebaseUser? = user.value

    fun isUserAnonymous(): Boolean = user.value == null
}