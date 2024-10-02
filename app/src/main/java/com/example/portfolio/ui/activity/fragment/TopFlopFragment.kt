package com.example.portfolio.ui.activity.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.portfolio.R
import com.example.portfolio.domain.model.Portfolio

class TopFlopFragment : Fragment(R.layout.fragment_top_flop) {

    private lateinit var portfolio: Portfolio

    companion object {
        private const val ARG_PORTFOLIO = "portfolio"

        fun newInstance(portfolio: Portfolio): TopFlopFragment {
            val fragment = TopFlopFragment()
            val args = Bundle()
            args.putParcelable(ARG_PORTFOLIO, portfolio)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            portfolio = it.getParcelable(ARG_PORTFOLIO) ?: throw IllegalArgumentException("Portfolio is required")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_flop, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topStocks = portfolio.getTop3()
        val flopStocks = portfolio.getFlop3()

        val topStocksContainer = view.findViewById<LinearLayout>(R.id.topStocksContainer)
        val flopStocksContainer = view.findViewById<LinearLayout>(R.id.flopStocksContainer)

        for (stock in topStocks) {
            val stockView = LayoutInflater.from(context).inflate(R.layout.item_stock_performance, topStocksContainer, false)
            val stockNameTextView = stockView.findViewById<TextView>(R.id.stockNameTextView)
            val stockPercentageTextView = stockView.findViewById<TextView>(R.id.stockPercentageTextView)

            stockNameTextView.text = stock.getStockName()
            stockPercentageTextView.text = view.context.getString(R.string.percentage_format, stock.getPlusMinusValueAsPercentage())

            stockPercentageTextView.setTextColor(if (stock.getPlusMinusValueAsPercentage() >= 0) {
                requireContext().getColor(android.R.color.holo_green_dark)
            } else {
                requireContext().getColor(android.R.color.holo_red_dark)
            })

            topStocksContainer.addView(stockView)
        }

        for (stock in flopStocks) {
            val stockView = LayoutInflater.from(context).inflate(R.layout.item_stock_performance, flopStocksContainer, false)
            val stockNameTextView = stockView.findViewById<TextView>(R.id.stockNameTextView)
            val stockPercentageTextView = stockView.findViewById<TextView>(R.id.stockPercentageTextView)

            stockNameTextView.text = stock.getStockName()
            stockPercentageTextView.text = view.context.getString(R.string.percentage_format, stock.getPlusMinusValueAsPercentage())
            stockPercentageTextView.setTextColor(if (stock.getPlusMinusValueAsPercentage() >= 0) {
                requireContext().getColor(android.R.color.holo_green_dark)
            } else {
                requireContext().getColor(android.R.color.holo_red_dark)
            })

            flopStocksContainer.addView(stockView)
        }
    }
}
