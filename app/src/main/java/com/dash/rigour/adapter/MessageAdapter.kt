package com.example.myfirsttask.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dash.rigour.R
import com.dash.rigour.activity.ChatActivity
import com.dash.rigour.activity.MessageActivity
import com.dash.rigour.data.User
import com.dash.rigour.data.UserInfo
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(private val context: ChatActivity, private val userList: ArrayList<UserInfo>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = userList[position]
        holder.txtUserName.text = user.firstName

        Glide.with(context).load(user.userImage).placeholder(R.drawable.profile).into(holder.image)


        holder.layoutUser.setOnClickListener {
            val intent: Intent = Intent(context, MessageActivity::class.java)

            intent.putExtra("userId", user.userId)

            intent.putExtra("userName", user.firstName)
            context.startActivity(intent)
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.userName)
        val txtMessage: TextView = view.findViewById(R.id.temp)
        val image: CircleImageView = view.findViewById(R.id.userImage)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)

    }

}