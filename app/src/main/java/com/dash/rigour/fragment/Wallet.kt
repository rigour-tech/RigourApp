package com.dash.rigour.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dash.rigour.R
import com.dash.rigour.adapter.TransactionAdapter
import com.dash.rigour.data.TransactionHistory
import com.dash.rigour.databinding.FragmentWalletBinding


class Wallet : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.receive.setOnClickListener {
            Toast.makeText(requireContext(), "Verify Your BVN In Settings", Toast.LENGTH_SHORT)
                .show()
        }
        binding.send.setOnClickListener {
            Toast.makeText(requireContext(), "Verify Your BVN In Settings", Toast.LENGTH_SHORT)
                .show()
        }
        binding.topUp.setOnClickListener {
            Toast.makeText(requireContext(), "Verify Your BVN In Settings", Toast.LENGTH_SHORT)
                .show()
        }
        binding.invoice.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon ....", Toast.LENGTH_SHORT)
                .show()
        }

        binding.editText.inputType = InputType.TYPE_NULL
        binding.editText.setText("₦700,000.000")


        val transaction = ArrayList<TransactionHistory>()

        for (i in 1..50) {
            transaction.add(
                TransactionHistory(
                    R.drawable.userimage,
                    "Received from Sherrif",
                    "09:57",
                    "₦700,000.000",
                    +i
                )
            )
        }

        val adapterT = TransactionAdapter(transaction)

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.historyRecyclerView.setHasFixedSize(true)

        binding.historyRecyclerView.adapter = adapterT


        return view
    }


}