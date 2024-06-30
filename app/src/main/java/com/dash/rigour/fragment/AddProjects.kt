package com.dash.rigour.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dash.rigour.R
import com.dash.rigour.data.JobsInfo
import com.dash.rigour.databinding.FragmentAddProjectsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class AddProjects : Fragment() {
    private var _binding: FragmentAddProjectsBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddProjectsBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()


        val languages = resources.getStringArray(R.array.ProjectType)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdownmenu, languages)
        val autoCompleteTv = binding.dropMenu
        autoCompleteTv.setAdapter(arrayAdapter)


        binding.postJob.setOnClickListener {
            if (isSpinnerEmpty(binding.dropMenu)) {
                Toast.makeText(requireContext(), "Select An Item", Toast.LENGTH_LONG).show()
            }
            if (binding.descriptionEt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Description Cant Be Empty", Toast.LENGTH_LONG)
                    .show()
            }
            if (binding.workersNeededEt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Workers Needed Cant Be Empty", Toast.LENGTH_LONG)
                    .show()
            }
            if (binding.titleEt.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Tile Cant Be Empty", Toast.LENGTH_LONG).show()
            } else {
                val projectType = binding.dropMenu.text.toString()
                val projectDescription = binding.dropMenu.text.toString()
                val projectTitle = binding.dropMenu.text.toString()
                val workersNeeded = binding.dropMenu.text.toString()

                val jobPosted =
                    JobsInfo(projectType, projectDescription, projectTitle, workersNeeded)

                databaseReference.child(uid).setValue(jobPosted).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showSnackbar("Job Posted")
                        Navigation.findNavController(view)
                            .navigate(R.id.action_addProjects_to_dashboardFragment2)
                    }
                }.addOnFailureListener {

                    showSnackbar("Job Not Posted")

                }

            }


        }


        return view
    }

    fun isSpinnerEmpty(autoCompleteTextView: AutoCompleteTextView): Boolean {
        val adapter = autoCompleteTextView.adapter
        return adapter == null || adapter.count == 0
    }

    private fun showSnackbar(message: String) {
        val rootView = binding.root
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
        Snackbar.ANIMATION_MODE_SLIDE
    }


}