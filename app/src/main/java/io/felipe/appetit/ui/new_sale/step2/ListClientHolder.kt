package io.felipe.appetit.ui.new_sale.step2

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.felipe.appetit.R
import io.felipe.appetit.database.Client
import io.felipe.appetit.databinding.ItemClientLayoutBinding
import kotlinx.android.synthetic.main.item_client_layout.view.*

class ListClientHolder(
    private val bind: ItemClientLayoutBinding,
    var onClick: (Client) -> Unit
) : RecyclerView.ViewHolder(bind.root) {

    fun bind(item: Client) = with(bind.root) {
        bind.client = item
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
        root.itemClientContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorPrimary
            )
        )
        root.itemClientName.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorWhite
            )
        )
    }

    private fun turnDecorateItemViewOff(root: View) {
        root.itemClientContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorWhite
            )
        )
        root.itemClientName.setTextColor(
            ContextCompat.getColor(
                root.context,
                R.color.colorBlack
            )
        )
    }
}