package com.dash.rigour.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dash.rigour.activity.HomeActivity
import com.dash.rigour.data.UserInfo
import com.dash.rigour.databinding.FragmentUsersInfoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class UsersInfo : Fragment() {
    private var _binding: FragmentUsersInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentUsersInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser?.uid.toString()

        binding.BtnContinue.setOnClickListener {

            if (binding.firstNameEt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Input Your First Name", Toast.LENGTH_SHORT).show()
                binding.firstName.requestFocus()
            }

            if (binding.lastNameEt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Input Your Last Name", Toast.LENGTH_SHORT).show()
                binding.lastName.requestFocus()
            }
            if (binding.phoneNumberEt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Input Your Correct Number", Toast.LENGTH_SHORT)
                    .show()
                binding.psw.requestFocus()
            } else {


                val sharedPreferences =
                    activity?.getSharedPreferences("info", Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.putString("first_name", binding.firstNameEt.text.toString())
                editor?.putString("last_name", binding.lastNameEt.text.toString())
                editor?.putString("phone_number", binding.phoneNumberEt.text.toString())
                editor?.apply()
                val firstName = binding.firstNameEt.text.toString()
                val lastName = binding.lastNameEt.text.toString()
                val phoneNumber = binding.phoneNumberEt.text.toString()


                val users = UserInfo(firstName, lastName, phoneNumber)

                databaseReference.child(uid).setValue(users).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showSnackbar("Account Successfully Created")
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        activity?.startActivity(intent)
                    }
                }.addOnFailureListener {

                    showSnackbar("Account Not Created")

                }
            }
        }




        return view
    }

    private fun showSnackbar(message: String) {
        val rootView = binding.root
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
        Snackbar.ANIMATION_MODE_SLIDE
    }

}