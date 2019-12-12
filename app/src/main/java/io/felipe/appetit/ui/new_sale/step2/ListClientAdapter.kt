package io.felipe.appetit.ui.new_sale.step2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.felipe.appetit.database.Client
import io.felipe.appetit.databinding.ItemClientLayoutBinding

class ListClientAdapter(var items: List<Client>, var onClick: (Client) -> Unit) :
    RecyclerView.Adapter<ListClientHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListClientHolder(
        ItemClientLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: ListClientHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    fun update(newList: List<Client>?) {
        items = newList ?: emptyList()
        this.notifyDataSetChanged()
    }
}