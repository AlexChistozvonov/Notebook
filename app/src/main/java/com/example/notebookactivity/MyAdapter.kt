package com.example.notebookactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(listMain: ArrayList<String>): RecyclerView.Adapter<MyAdapter.MyHolder>() {

    var listArray = listMain

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        fun setData(title: String){
            tvTitle.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }


    fun updateAdapter(listItems:List<String>){

        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

}