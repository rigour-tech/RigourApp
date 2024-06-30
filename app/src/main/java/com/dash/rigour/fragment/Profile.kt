package com.dash.rigour.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dash.rigour.activity.MainActivity
import com.dash.rigour.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class Profile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        val sharedPreferences = activity?.getSharedPreferences("info", Context.MODE_PRIVATE)
        val lastName = sharedPreferences?.getString("last_name", "").toString()
        val firstName = sharedPreferences?.getString("first_name", "").toString()

        binding.textView10.text = "$firstName $lastName"


        binding.logout.setOnClickListener {
            if (auth.currentUser != null) {
                auth.signOut()
                Toast.makeText(requireContext(), "Log Out Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                activity?.startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Cant Log Out", Toast.LENGTH_SHORT).show()

            }




        }
        binding.termsAndPrivacy.setOnClickListener {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://gideonjones.blogspot.com/2024/06/terms-and-conditions-for-payment-on.html")
            )
            activity?.startActivity(urlIntent)
        }


        return view
    }

}