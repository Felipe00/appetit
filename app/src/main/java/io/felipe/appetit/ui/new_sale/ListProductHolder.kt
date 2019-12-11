package io.felipe.appetit.ui.new_sale

import androidx.recyclerview.widget.RecyclerView
import io.felipe.appetit.database.Product
import io.felipe.appetit.databinding.ItemProductLayoutBinding

class ListProductHolder(private val bind: ItemProductLayoutBinding, var onClick: (Product) -> Unit) :
    RecyclerView.ViewHolder(bind.root) {

    fun bind(item: Product) = with(bind.root) {
        bind.product = item
        bind.executePendingBindings()

        setOnClickListener {
            onClick.invoke(item)
        }
    }
}