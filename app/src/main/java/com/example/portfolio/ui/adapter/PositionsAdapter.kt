package com.example.portfolio.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.domain.EvaluatedPosition

// Adapter class for Position items
class PositionAdapter(
    private val positions: List<EvaluatedPosition>
) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    // ViewHolder class to represent each item in the RecyclerView
    class PositionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockTitle: TextView = itemView.findViewById(R.id.stockTitle)
        val currentPriceValue: TextView = itemView.findViewById(R.id.currentPriceValue)
        val quantityValue: TextView = itemView.findViewById(R.id.quantityValue)
        val estimationValue: TextView = itemView.findViewById(R.id.estimationValue)
        val buyPriceValue: TextView = itemView.findViewById(R.id.buyPriceValue)
        val plusMinusValue: TextView = itemView.findViewById(R.id.plusMinusValue)
        val plusMinusPercentage: TextView = itemView.findViewById(R.id.plusMinusPercentage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_position, parent, false)
        return PositionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        val currentItem = positions[position]

        // Set the stock title
        holder.stockTitle.text = currentItem.position.stock.name

        // Set the course value (current price)
        holder.currentPriceValue.text = holder.itemView.context.getString(R.string.cours_format, currentItem.currentPrice)

        // Set the quantity value (number of stocks)
        holder.quantityValue.text = currentItem.position.number.toString()

        // Set the estimation value (current price * number of stocks)
        val estimation = currentItem.currentPrice * currentItem.position.number.toFloat()
        holder.estimationValue.text = holder.itemView.context.getString(R.string.estimation_format, estimation)

        // Set the buy price (prix achat)
        holder.buyPriceValue.text = holder.itemView.context.getString(R.string.buyprice_format, currentItem.position.buy)

        // Calculate and set the +/- value (current estimation - buy price)
        val plusMinusValue = estimation - currentItem.position.buy
        holder.plusMinusValue.text = holder.itemView.context.getString(R.string.plusminusvalue_format, plusMinusValue)

        val plusMinusPercentage = plusMinusValue / estimation * 100
        holder.plusMinusPercentage.text = holder.itemView.context.getString(R.string.percentage_format, plusMinusPercentage)


        if (plusMinusValue >= 0) {
            // Positive or zero: Green color
            holder.plusMinusValue.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
            holder.plusMinusPercentage.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
        } else {
            // Negative: Red color
            holder.plusMinusValue.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
            holder.plusMinusPercentage.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
        }
    }

    override fun getItemCount(): Int {
        return positions.size
    }
}
