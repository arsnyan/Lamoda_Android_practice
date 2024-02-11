package com.arsnyan.lamodacopy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.Category
import com.arsnyan.lamodacopy.data.CategoryRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    lateinit var firebaseAuth: FirebaseAuth
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: Flow<FirebaseUser?> = _user

    private val _categories = MutableStateFlow(listOf<Category>())
    val categories: StateFlow<List<Category>> = _categories

    fun setUser(user: FirebaseUser?) {
        _user.value = user
    }

    fun setCategories(list: List<Category>) {
        _categories.value = list
    }

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data = categoryRepository.getCategories()
                _categories.emit(data)
            }
        }
    }
}