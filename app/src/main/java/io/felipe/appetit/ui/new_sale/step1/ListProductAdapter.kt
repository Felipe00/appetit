package io.felipe.appetit.ui.new_sale.step1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.felipe.appetit.database.Product
import io.felipe.appetit.databinding.ItemProductLayoutBinding

class ListProductAdapter(var items: List<Product>, var onClick: (Product) -> Unit) :
    RecyclerView.Adapter<ListProductHolder>() {

    private var currentCategory = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListProductHolder(
        ItemProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: ListProductHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    fun update(newList: List<Product>?) {
        currentCategory = ""
        items = newList?.map {
            when {
                currentCategory.isEmpty() || currentCategory != it.category -> {
                    currentCategory = it.category ?: ""
                    it.isSection = true
                }
                else -> {
                    it.isSection = false
                }
            }
            it
        } ?: emptyList()
        this.notifyDataSetChanged()
    }
}