package com.example.portfolio.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.domain.model.Dividend

class DividendsAdapter(private val dividendsList: List<Dividend>) :
    RecyclerView.Adapter<DividendsAdapter.DividendViewHolder>() {

    class DividendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockNameTextView: TextView = itemView.findViewById(R.id.stockNameTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DividendViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dividend, parent, false)
        return DividendViewHolder(view)
    }

    @RequiresApi(value = 26)
    override fun onBindViewHolder(holder: DividendViewHolder, position: Int) {
        val dividend = dividendsList[position]
        holder.stockNameTextView.text = dividend.stock.name
        holder.amountTextView.text =
            holder.itemView.context.getString(R.string.euro_format, dividend.amount)
        holder.dateTextView.text = dividend.readableDate()
    }

    override fun getItemCount(): Int = dividendsList.size
}
