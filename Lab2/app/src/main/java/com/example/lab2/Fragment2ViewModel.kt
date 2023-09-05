package com.example.lab2

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class Fragment2ViewModel : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _color : MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))

    @RequiresApi(Build.VERSION_CODES.O)
    public val color  = _color as LiveData<Color>

    @RequiresApi(Build.VERSION_CODES.O)
    fun pickColor(){
        with(Random.Default) {
            _color.value = Color.valueOf(nextFloat(), nextFloat(), nextFloat())
        }
    }

}