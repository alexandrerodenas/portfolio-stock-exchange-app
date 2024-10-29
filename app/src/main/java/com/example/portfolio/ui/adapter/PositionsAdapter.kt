package com.example.portfolio.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.domain.model.EvaluatedPosition

class PositionAdapter(
    private val evaluatedPositions: List<EvaluatedPosition>,
    private val onItemClicked: (EvaluatedPosition) -> Unit,
    private val onEditClicked: (EvaluatedPosition) -> Unit
) : RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    class PositionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockTitle: TextView = itemView.findViewById(R.id.stockTitle)
        val currentPriceValue: TextView = itemView.findViewById(R.id.currentPriceValue)
        val quantityValue: TextView = itemView.findViewById(R.id.quantityValue)
        val estimationValue: TextView = itemView.findViewById(R.id.estimationValue)
        val buyPriceValue: TextView = itemView.findViewById(R.id.buyPriceValue)
        val plusMinusValue: TextView = itemView.findViewById(R.id.plusMinusValue)
        val plusMinusPercentage: TextView = itemView.findViewById(R.id.plusMinusPercentage)
        val showChartIcon: ImageView = itemView.findViewById(R.id.showChartIcon)
        val editIcon: ImageView = itemView.findViewById(R.id.editIcon) // Add this icon to XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_position, parent, false)
        return PositionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        val currentEvaluatedPosition = evaluatedPositions[position]

        holder.stockTitle.text = currentEvaluatedPosition.getStockName()
        holder.currentPriceValue.text = holder.itemView.context.getString(R.string.euro_format, currentEvaluatedPosition.currentPrice)
        holder.quantityValue.text = currentEvaluatedPosition.getPositionCount().toString()
        holder.estimationValue.text = holder.itemView.context.getString(R.string.euro_format, currentEvaluatedPosition.getEstimation())
        holder.buyPriceValue.text = holder.itemView.context.getString(R.string.euro_format, currentEvaluatedPosition.position.buy)
        holder.plusMinusValue.text = holder.itemView.context.getString(R.string.euro_format, currentEvaluatedPosition.getPlusMinusValue())
        holder.plusMinusPercentage.text = holder.itemView.context.getString(R.string.percentage_format, currentEvaluatedPosition.getPlusMinusValueAsPercentage())

        if (currentEvaluatedPosition.getPlusMinusValue() >= 0) {
            holder.plusMinusValue.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
            holder.plusMinusPercentage.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
        } else {
            holder.plusMinusValue.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
            holder.plusMinusPercentage.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
        }

        holder.showChartIcon.setOnClickListener {
            onItemClicked(currentEvaluatedPosition)
        }

        holder.editIcon.setOnClickListener {
            onEditClicked(currentEvaluatedPosition)
        }
    }

    override fun getItemCount(): Int {
        return evaluatedPositions.size
    }
}
