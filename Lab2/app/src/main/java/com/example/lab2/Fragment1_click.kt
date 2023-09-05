package com.example.lab2

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.lab2.databinding.Fragment1ClickBinding

class Fragment1_click : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = Fragment1ClickBinding.inflate(inflater, container, false)
        val viewModel : Fragment2ViewModel by activityViewModels()

        // change to fragment2 when clicking
        binding.clickMe.setOnClickListener{
//            viewModel.pickColor()

            val drawFragment = Fragment2_draw()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, drawFragment, "draw_tag")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return binding.root
    }

}