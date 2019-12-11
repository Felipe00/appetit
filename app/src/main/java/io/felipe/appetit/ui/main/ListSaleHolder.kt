package io.felipe.appetit.ui.main

import androidx.recyclerview.widget.RecyclerView
import io.felipe.appetit.database.Sale
import io.felipe.appetit.databinding.ItemSalesLayoutBinding

class ListSaleHolder(private val bind: ItemSalesLayoutBinding, var onClick: (Sale) -> Unit) :
    RecyclerView.ViewHolder(bind.root) {

    fun bind(item: Sale, header: String) = with(bind.root) {
        bind.sale = item
        bind.header = header
        bind.executePendingBindings()

        setOnClickListener {
            onClick.invoke(item)
        }
    }
}