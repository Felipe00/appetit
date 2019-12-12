package io.felipe.appetit.ui.new_sale.step1

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.felipe.appetit.R
import io.felipe.appetit.database.Database
import io.felipe.appetit.database.PrefsDb
import io.felipe.appetit.database.ProductsSold
import io.felipe.appetit.ui.new_sale.NewSaleActivity
import kotlinx.android.synthetic.main.fragment_choose_product.*
import java.text.NumberFormat
import java.util.*

class ChooseProductFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var productAdapter: ListProductAdapter
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = PrefsDb.init(activity!!).getDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(chooseProductList) {
            productAdapter = ListProductAdapter(database.sortedProducts ?: emptyList()) {
                listener?.onFragmentInteraction(it, NewSaleActivity.PRODUCT)
            }
            adapter = productAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        chooseProductSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                peformSearch(s.toString())
            }
        })
        chooseProductNextContainer.setOnClickListener {
            (activity as NewSaleActivity).callClientsFragment()
        }
    }

    private fun peformSearch(text: String) {
        productAdapter.update(database.sortedProducts?.filter {
            it.name?.toUpperCase(Locale.getDefault())?.contains(text.toUpperCase(Locale.getDefault())) == true
        })
    }

    fun updateList(productSold: ProductsSold) {
        productAdapter.update(emptyList())
        productAdapter.update(
            database.sortedProducts?.map {
                if (it.id == productSold.idProduct) {
                    it.isSelected = true
                }
                it
            }
        )
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val selectedItems = (activity as NewSaleActivity).getProductSoldList()
        if (selectedItems.isEmpty()) {
            turnTotalPriceViewOff()
        } else {
            chooseProductTotal.text = NumberFormat.getCurrencyInstance().format(
                selectedItems.sumByDouble {
                    ((it.price ?: 0) * (it.quantity ?: 0)).toDouble() / 100
                }
            )
            turnTotalPriceViewOn()
        }
    }

    private fun turnTotalPriceViewOn() {
        chooseProductTotalContainer.visibility = View.VISIBLE
    }

    private fun turnTotalPriceViewOff() {
        chooseProductTotalContainer.visibility = View.GONE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(item: Any, type: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance() = ChooseProductFragment().apply {}
    }
}
