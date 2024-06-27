package com.dash.rigour.fragment

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dash.rigour.R
import com.dash.rigour.activity.HomeActivity
import com.dash.rigour.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root


        val editText = binding.passwordEt.text
        editText?.filters = arrayOf(InputFilter.LengthFilter(10))

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.login.setOnClickListener {

            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches()) {
                Toast.makeText(requireContext(), "Incorrect Email", Toast.LENGTH_SHORT).show()
                binding.emailAddress.requestFocus()
            }
            if (binding.passwordEt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Password Field Cant Be Empty", Toast.LENGTH_SHORT)
                    .show()
                binding.psw.requestFocus()
            } else {
                auth.signInWithEmailAndPassword(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
                    .addOnCompleteListener {
                        if (currentUser != null) {
                            if (currentUser.isEmailVerified)
                                Toast.makeText(
                                    requireContext(),
                                    "Login Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            activity?.startActivity(intent)

                        }


                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Cant Login", Toast.LENGTH_SHORT).show()
                    }
            }
            binding.signUp.setOnClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_signUpFragment)
            }

        }

        return view
    }

}