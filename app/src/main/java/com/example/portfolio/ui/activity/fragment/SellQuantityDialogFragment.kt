package com.example.portfolio.ui.activity.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.portfolio.R
import com.example.portfolio.ui.utils.DatePickerCreator

class SellQuantityDialogFragment(
    private val maxQuantity: Int,
    private val currentPrice: Double,
    private val onSubmit: (Int, Double, String) -> Unit
) : DialogFragment() {

    private lateinit var quantityTextView: TextView
    private lateinit var addButton: ImageView
    private lateinit var subtractButton: ImageView
    private lateinit var dateEditText: EditText
    private lateinit var sellingPriceEditText: EditText
    private var quantity: Int = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_quantity, null)

        quantityTextView = dialogView.findViewById(R.id.quantityTextView)
        addButton = dialogView.findViewById(R.id.addButton)
        subtractButton = dialogView.findViewById(R.id.subtractButton)
        dateEditText = dialogView.findViewById(R.id.dateEditText)
        sellingPriceEditText = dialogView.findViewById(R.id.sellingPriceEditText)

        quantityTextView.text = quantity.toString()
        sellingPriceEditText.setText(currentPrice.toString())
        dateEditText.setText(DatePickerCreator.getDateOfNow())


        addButton.setOnClickListener {
            if (quantity < maxQuantity) {
                quantity++
                quantityTextView.text = quantity.toString()
            }
        }

        subtractButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        dateEditText.setOnClickListener {
            DatePickerCreator.create(requireContext()) { date ->
                dateEditText.setText(date.toString())
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Vendre")
            .setPositiveButton("Confirmer") { _, _ ->
                val sellPrice = sellingPriceEditText.text.toString().toDoubleOrNull() ?: 0.0
                val sellDate = dateEditText.text.toString()
                onSubmit(quantity, sellPrice, sellDate)
            }
            .setNegativeButton("Annuler", null)
            .create()
    }
}
