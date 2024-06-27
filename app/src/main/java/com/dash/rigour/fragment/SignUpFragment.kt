package com.dash.rigour.fragment

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
import com.dash.rigour.databinding.FragmentSignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root


        val editText = binding.passwordEt.text
        editText?.filters = arrayOf(InputFilter.LengthFilter(10))

        auth = FirebaseAuth.getInstance()
        binding.getStarted.setOnClickListener {


            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches()) {
                Toast.makeText(requireContext(), "Input Correct Email Address", Toast.LENGTH_SHORT)
                    .show()
                binding.emailAddress.requestFocus()
            }
            if (binding.passwordEt.text.toString().length < 10) {
                Toast.makeText(
                    requireContext(),
                    "Password Must Be 10 Digit",
                    Toast.LENGTH_SHORT
                ).show()
                binding.psw.requestFocus()

            }
            if (!(binding.passwordEt.text.toString()
                    .contains(
                        Regex("(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])")
                    )
                        )
            ) {
                Toast.makeText(
                    requireContext(),
                    "Password Must Contain Small letters, UpperCase Letters and Numbers",
                    Toast.LENGTH_SHORT
                ).show()
                binding.psw.requestFocus()

            } else {
                auth.createUserWithEmailAndPassword(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = Firebase.auth.currentUser
                            user!!.sendEmailVerification()
                            Toast.makeText(
                                requireContext(),
                                "Account Created, Check Your Email",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Account Not Created",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


            }
        }


        binding.login.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        return view
    }


}
