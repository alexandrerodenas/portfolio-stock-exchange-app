package com.example.portfolio.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.domain.model.EvaluatedPosition

class PositionAdapter(
    private val evaluatedPositions: List<EvaluatedPosition>,
    private val onItemClicked: (EvaluatedPosition) -> Unit
) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

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
        val currentItem = evaluatedPositions[position]

        // Set the stock title
        holder.stockTitle.text = currentItem.position.stock.name

        // Set the course value (current price)
        holder.currentPriceValue.text = holder.itemView.context.getString(R.string.euro_format, currentItem.currentPrice)

        // Set the quantity value (number of stocks)
        holder.quantityValue.text = currentItem.position.number.toString()

        // Set the estimation value (current price * number of stocks)
        val estimation = currentItem.getEstimation()
        holder.estimationValue.text = holder.itemView.context.getString(R.string.euro_format, estimation)

        // Set the buy price
        holder.buyPriceValue.text = holder.itemView.context.getString(R.string.euro_format, currentItem.position.buy)

        // Calculate and set the +/- value (current estimation - buy price)
        val plusMinusValue = currentItem.getPlusMinusValue()
        holder.plusMinusValue.text = holder.itemView.context.getString(R.string.euro_format, plusMinusValue)

        val plusMinusPercentage = currentItem.getPlusMinusValueAsPercentage()
        holder.plusMinusPercentage.text = holder.itemView.context.getString(R.string.percentage_format, plusMinusPercentage)


        if (plusMinusValue >= 0) {
            holder.plusMinusValue.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
            holder.plusMinusPercentage.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
        } else {
            holder.plusMinusValue.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
            holder.plusMinusPercentage.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
        }

        holder.itemView.setOnClickListener {
            onItemClicked(currentItem)
        }
    }


    override fun getItemCount(): Int {
        return evaluatedPositions.size
    }
}
