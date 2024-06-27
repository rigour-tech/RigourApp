package com.dash.rigour.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

import com.dash.rigour.R
import com.dash.rigour.databinding.FragmentSecondScreenBinding


class SecondScreen : Fragment() {
    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        Handler().postDelayed({
            Navigation.findNavController(view)
                .navigate(R.id.action_secondScreen_to_viewPagerFragment)

        }, 4000)


        return view
    }



}