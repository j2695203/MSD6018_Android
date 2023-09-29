package com.example.lab4

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class JokeViewModel(private val repository: JokeRepository) : ViewModel() {

    val currentJoke: LiveData<JokeData> = repository.currentJoke

    val allJoke: LiveData<List<JokeData>> = repository.allJoke

    fun checkJoke(city: String){
        repository.checkJoke(city)
    }

}

// This factory class allows us to define "custom constructors" for the view model (don't try to figure out..)
class JokeViewModelFactory(private val repository: JokeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JokeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JokeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
