package com.dash.rigour.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dash.rigour.R
import com.dash.rigour.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth


class ForgetPassword : Fragment() {
    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.forgetPswBtn.setOnClickListener {

            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches()) {
                binding.emailAddress.requestFocus()
                Toast.makeText(requireContext(), "Wrong Email Address", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(binding.emailEt.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireContext(), "Check Your Email", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(requireContext(), "Unsuccessful", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


            }

            binding.login.setOnClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_forgetPassword_to_loginFragment)
            }
        }

        return view
    }

}