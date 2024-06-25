package com.dash.rigour.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.dash.rigour.R
import com.dash.rigour.databinding.FragmentUsersInfoBinding
import com.google.firebase.auth.FirebaseAuth


class UsersInfo : Fragment() {
private var _binding : FragmentUsersInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_info, container, false)
    }

}