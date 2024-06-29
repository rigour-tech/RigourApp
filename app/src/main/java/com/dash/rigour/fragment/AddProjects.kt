package com.dash.rigour.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dash.rigour.R
import com.dash.rigour.databinding.FragmentAddProjectsBinding


class AddProjects : Fragment() {
    private var _binding: FragmentAddProjectsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddProjectsBinding.inflate(inflater,container,false)
        val view = binding.root

if


        return view
    }


}