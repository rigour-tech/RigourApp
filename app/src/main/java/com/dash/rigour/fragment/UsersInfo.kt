package com.dash.rigour.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dash.rigour.databinding.FragmentUsersInfoBinding
import com.google.firebase.auth.FirebaseAuth


class UsersInfo : Fragment() {
    private var _binding: FragmentUsersInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentUsersInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.BtnContinue.setOnClickListener {

            if (binding.firstNameEt.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Input Your First Name", Toast.LENGTH_SHORT).show()
                binding.firstName.requestFocus()
            }

            if (binding.lastNameEt.text.toString().isEmpty()){

            }


        }




        return view
    }

}