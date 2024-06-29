package com.dash.rigour.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dash.rigour.R
import com.dash.rigour.data.TransactionHistory

class TransactionAdapter(private val transactionList: List<TransactionHistory>) :
    RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleImage: ImageView = itemView.findViewById(R.id.imageView10)
        val titleTraction: TextView = itemView.findViewById(R.id.userSent)
        val titleTime: TextView = itemView.findViewById(R.id.time)
        val titleAmount: TextView = itemView.findViewById(R.id.amount)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction, parent, false)


        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {

        return transactionList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val currentItem = transactionList[position]

        holder.titleImage.setImageResource(currentItem.image)
        holder.titleTraction.text = currentItem.received
        holder.titleTime.text = currentItem.time
        holder.titleAmount.text = currentItem.amount


    }
}