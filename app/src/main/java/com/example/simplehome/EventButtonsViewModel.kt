package com.example.simplehome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplehome.models.UserTest

class EventButtonsViewModel : ViewModel() {
    // TODO: Implement the ViewModel

  /*  private val test: MutableLiveData<List<String>> by lazy {
        test.also {
        loadTest()
        }
    }

    fun getTest(): LiveData<List<String>>{
        return test
    }

    private fun loadTest(){
       val test = listOf(2,3,4,5,6,7,8)
    }
*/
    fun getUserTest(): LiveData<UserTest>{
        return userTest
    }

    private val userTest: MutableLiveData<UserTest> by lazy {
        userTest.also { loaduser() }
    }

    private fun loaduser() {
        val userTest = UserTest("test")
    }


}

