package com.example.portfolio.ui.activity.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.portfolio.R
import com.example.portfolio.domain.model.Portfolio

class GlobalPlusMinusValueFragment : Fragment(R.layout.fragment_global_plus_minus_value) {

    companion object {
        private const val ARG_PORTFOLIO = "portfolio"

        fun newInstance(portfolio: Portfolio): GlobalPlusMinusValueFragment {
            val fragment = GlobalPlusMinusValueFragment()
            val bundle = Bundle().apply {
                putParcelable(ARG_PORTFOLIO, portfolio)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val investmentValue: TextView = view.findViewById(R.id.investmentValue)
        val estimationValue: TextView = view.findViewById(R.id.estimationValue)
        val pendingPlusMinusValue: TextView = view.findViewById(R.id.pendingPlusMinusValue)

        val portfolio = arguments?.getParcelable<Portfolio>(ARG_PORTFOLIO)!!
        val totalInvestment = portfolio.getTotalInvestment()
        val totalEstimation = portfolio.getTotalEstimation()
        val pending = portfolio.getPendingPlusMinusValue()

        investmentValue.text = getString(R.string.euro_format, totalInvestment)
        estimationValue.text = getString(R.string.euro_format, totalEstimation)
        pendingPlusMinusValue.text = getString(R.string.euro_format, pending)

        pendingPlusMinusValue.setTextColor(
            if (totalInvestment <= totalEstimation)
                requireContext().getColor(android.R.color.holo_green_dark)
            else
                requireContext().getColor(android.R.color.holo_red_dark)
        )
    }

}