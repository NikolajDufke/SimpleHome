package com.example.simplehome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.simplehome.models.UserTest

class EventButtonsViewModel : ViewModel() {
    // TODO: Implement the ViewModel

private val _userId: MutableLiveData<String> = MutableLiveData()

    val user: LiveData<User> = Transformations.switchMap(_userId) {}

}

