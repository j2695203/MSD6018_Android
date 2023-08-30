package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.example.myapplication.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // use data binding instead of findViewById
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // get data from intent
        val pass = intent.extras!!
        val showText = pass.getString("text")
        binding.textView3.text = showText

        // click button
        binding.backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}