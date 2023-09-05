package com.example.lab2

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.lab2.databinding.Fragment2DrawBinding

class Fragment2_draw : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = Fragment2DrawBinding.inflate(inflater)

        val viewModel : Fragment2ViewModel by activityViewModels()
        viewModel.color.observe(viewLifecycleOwner){
            binding.customView.drawCircle(it)
        }

        // change to fragment1 when clicking
        binding.back.setOnClickListener{

            val clickFragment = Fragment1_click()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, clickFragment, "home_tag")
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.customView.setOnTouchListener { v, event ->
           viewModel.pickColor();
            true
        }

        return binding.root
    }
}