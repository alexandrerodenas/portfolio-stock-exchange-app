package com.example.portfolio.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.R
import com.example.portfolio.database.AppDatabase
import com.example.portfolio.database.model.PositionDB
import com.example.portfolio.ui.utils.DatePickerCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePositionActivity : AppCompatActivity() {

    private lateinit var stockSymbolSpinner: Spinner
    private lateinit var numberOfSharesEditText: EditText
    private lateinit var buyPriceEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var submitPositionButton: Button

    private var selectedStockSymbol: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_position)

        stockSymbolSpinner = findViewById(R.id.stockSymbolSpinner)
        numberOfSharesEditText = findViewById(R.id.numberOfSharesEditText)
        buyPriceEditText = findViewById(R.id.buyPriceEditText)
        dateEditText = findViewById(R.id.dateEditText)
        submitPositionButton = findViewById(R.id.submitPositionButton)

        dateEditText.setText(DatePickerCreator.getDateOfNow())
        dateEditText.setOnClickListener {
            DatePickerCreator.create(this) { date -> dateEditText.setText(date.toString()) }
        }

        populateStockSpinner()

        submitPositionButton.setOnClickListener {
            val stockSymbol = selectedStockSymbol
            val numberOfShares = numberOfSharesEditText.text.toString().toIntOrNull()
            val buyPrice = buyPriceEditText.text.toString().toDoubleOrNull()
            val dateString = dateEditText.text.toString()

            if (stockSymbol.isNullOrEmpty() || numberOfShares == null || buyPrice == null || dateString.isEmpty()) {
                Toast.makeText(this, R.string.error_input_invalid, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val positionDB = PositionDB(
                stockSymbol = stockSymbol,
                number = numberOfShares,
                buy = buyPrice,
                date = dateString
            )

            insertPositionIntoDB(positionDB)
        }
    }

    private fun populateStockSpinner() {
        val stockDao = AppDatabase.getInstance(this).stockDao()

        lifecycleScope.launch {
            stockDao.getAllButIndex().collect { stockList ->
                val stocksNames = stockList.map { it.name }.sorted()
                val adapter = ArrayAdapter(
                    this@CreatePositionActivity,
                    android.R.layout.simple_spinner_item,
                    stocksNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                stockSymbolSpinner.adapter = adapter

                stockSymbolSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            selectedStockSymbol = stockList[position].symbol
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            selectedStockSymbol = null
                        }
                    }
            }
        }
    }

    private fun insertPositionIntoDB(positionDB: PositionDB) {
        val positionDao = AppDatabase.getInstance(this).positionDao()

        lifecycleScope.launch(Dispatchers.IO) {
            positionDao.insert(listOf(positionDB))
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@CreatePositionActivity,
                    R.string.position_added,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}
