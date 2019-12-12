package io.felipe.appetit.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.felipe.appetit.database.Sale
import io.felipe.appetit.databinding.ItemSalesLayoutBinding
import java.text.NumberFormat

class ListSaleAdapter(private var items: List<Sale>, var onClick: (Sale) -> Unit) :
    RecyclerView.Adapter<ListSaleHolder>() {

    private var currentDate = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListSaleHolder(
        ItemSalesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: ListSaleHolder, position: Int) =
        holder.bind(items[position], getHeaderText(position))

    override fun getItemCount() = items.size

    /**
     * Verifica se a data é nova (vazio) ou se é diferente da anterior.
     * Se for, ele envia um texto com data e valor total para aquela data.
     * Senão, ele envia uma texto vazio
     */
    private fun getHeaderText(position: Int): String = when {
        currentDate.isEmpty() || currentDate != items[position].soldAt -> {
            currentDate = items[position].soldAt ?: ""
            getFormattedText()
        }
        else -> ""
    }

    private fun getFormattedText(): String =
        "$currentDate, você já vendeu ${NumberFormat.getCurrencyInstance().format(
            getTotalPricePerDate()
        )}"

    private fun getTotalPricePerDate(): Double =
        items.filter { it.soldAt == currentDate }.flatMap { it.productsSold!! }.sumBy {
            (it.price ?: 0) * (it.quantity ?: 0)
        }.toDouble() / 100

    fun update(newList: List<Sale>?) {
        currentDate = ""
        items = newList ?: emptyList()
        this.notifyDataSetChanged()
    }
}