package com.example.portfolio.ui.activity.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.portfolio.R

class EditQuantityDialogFragment(
    private var quantity: Int,
    private val onQuantityUpdated: (Int) -> Unit
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_edit_quantity, container, false)

        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val addButton: ImageView = view.findViewById(R.id.addButton)
        val subtractButton: ImageView = view.findViewById(R.id.subtractButton)

        quantityTextView.text = quantity.toString()

        addButton.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
        }

        subtractButton.setOnClickListener {
            if (quantity > 0) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setPositiveButton("OK") { _, _ ->
                onQuantityUpdated(quantity)
            }
            .setNegativeButton("Annuler", null)
            .create()
    }
}
