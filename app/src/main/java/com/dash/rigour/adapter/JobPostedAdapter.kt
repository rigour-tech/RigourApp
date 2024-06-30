package com.dash.rigour.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dash.rigour.R
import com.dash.rigour.data.JobsInfo

class JobPostedAdapter(private val list: MutableList<JobsInfo>) :
    RecyclerView.Adapter<JobPostedAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val projectType: TextView = itemView.findViewById(R.id.projectType)
        val projectDescription: TextView = itemView.findViewById(R.id.projectDescription)
        val workersNeeded: TextView = itemView.findViewById(R.id.workerNeeded)
        val projectTitle: TextView = itemView.findViewById(R.id.productName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.projectslayout, parent, false)


        return JobPostedAdapter.MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {

        return list.size


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = list[position]

        holder.projectType.text = currentItem.projectType
        holder.projectDescription.text = currentItem.description
        holder.projectTitle.text = currentItem.projectTitle
        holder.workersNeeded.text = currentItem.workersNeeded


    }

}
