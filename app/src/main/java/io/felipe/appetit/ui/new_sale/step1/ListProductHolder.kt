package io.felipe.appetit.ui.new_sale.step1

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.felipe.appetit.R
import io.felipe.appetit.database.Product
import io.felipe.appetit.databinding.ItemProductLayoutBinding
import kotlinx.android.synthetic.main.item_product_layout.view.*

class ListProductHolder(
    private val bind: ItemProductLayoutBinding,
    var onClick: (Product) -> Unit
) : RecyclerView.ViewHolder(bind.root) {

    fun bind(item: Product) = with(bind.root) {
        bind.product = item
        bind.header = if (item.isSection == true) {
            item.category
        } else {
            ""
        }
        bind.executePendingBindings()
        if (item.isSelected != null && item.isSelected == true) {
            turnDecorateItemViewOn(this)
        } else {
            turnDecorateItemViewOff(this)
        }

        setOnClickListener {
            onClick.invoke(item)
        }
    }

    private fun turnDecorateItemViewOn(root: View) {
        root.itemProductContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorPrimary
            )
        )
        root.itemProductPrice.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorWhite
            )
        )
        root.itemProductName.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorWhite
            )
        )
        root.itemProductDescription.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorWhite
            )
        )
    }

    private fun turnDecorateItemViewOff(root: View) {
        root.itemProductContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorWhite
            )
        )
        root.itemProductPrice.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorBlack
            )
        )
        root.itemProductName.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorBlack
            )
        )
        root.itemProductDescription.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorBlack
            )
        )
    }
}