package io.felipe.appetit.ui.new_sale.step1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.transition.TransitionManager
import com.google.gson.Gson
import io.felipe.appetit.R
import io.felipe.appetit.database.Product
import io.felipe.appetit.database.ProductsSold
import kotlinx.android.synthetic.main.activity_detail_product.*
import java.text.NumberFormat
import java.util.*


class DetailProductActivity : AppCompatActivity() {

    private lateinit var selectedProduct: Product
    private var quantity = 1
    private var option = ""
    private var totalPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        selectedProduct = Gson().fromJson(intent.getStringExtra("product"), Product::class.java)
        detailProductName.text = selectedProduct.name
        detailProductPrice.text =
            NumberFormat.getCurrencyInstance().format((selectedProduct.price ?: 0).toDouble() / 100)
        if (selectedProduct.description?.isEmpty() == true) {
            TransitionManager.beginDelayedTransition(detailProductInfoCard)
            detailProductDescription.visibility = View.GONE
        } else {
            detailProductDescription.text = selectedProduct.description
        }
        detailProductTotalPrice.text = detailProductPrice.text.toString()
        // add radio buttons
        selectedProduct.options?.forEach {
            addRadioButtons(it)
        }
        detailProductAdd.setOnClickListener {
            quantity += 1
            if (quantity > 1) {
                turnRemoveViewOn()
            }
            updatePrice()
            detailProductQuantity.text = "$quantity"
        }
        detailProductRemove.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
            }
            if (quantity == 1) {
                turnRemoveViewOff()
            }
            updatePrice()
            detailProductQuantity.text = "$quantity"
        }
        detailProductNextContainer.setOnClickListener {
            if (option.isEmpty()) {
                Toast.makeText(
                    this@DetailProductActivity,
                    getString(R.string.detail_product_emption_option_error_toast),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val productSold = ProductsSold(
                id = Random().nextLong(),
                idProduct = selectedProduct.id,
                name = selectedProduct.name,
                price = selectedProduct.price,
                option = option,
                quantity = quantity,
                comments = detailProductComments.text.toString()
            )
            setResult(
                Activity.RESULT_OK,
                Intent().putExtra("productSold", Gson().toJson(productSold))
            )
            finish()
        }
        detailProductBack.setOnClickListener {
            finish()
        }
    }

    private fun updatePrice() {
        totalPrice = ((selectedProduct.price ?: 0).toDouble() / 100) * quantity
        detailProductTotalPrice.text = NumberFormat.getCurrencyInstance().format(totalPrice)
    }

    private fun turnRemoveViewOff() {
        DrawableCompat.setTint(
            DrawableCompat.wrap(detailProductRemove.drawable),
            ContextCompat.getColor(this@DetailProductActivity, R.color.colorGrey)
        )
    }

    private fun turnRemoveViewOn() {
        DrawableCompat.setTint(
            DrawableCompat.wrap(detailProductRemove.drawable),
            ContextCompat.getColor(this@DetailProductActivity, R.color.colorPrimary)
        )
    }

    private fun addRadioButtons(text: String) {
        val rbn = RadioButton(this@DetailProductActivity)
        val params = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 16, 0, 0)
        rbn.compoundDrawablePadding = 30
        rbn.layoutParams = params
        rbn.id = View.generateViewId()
        rbn.text = text
        rbn.background =
            ContextCompat.getDrawable(this@DetailProductActivity, R.drawable.outline_border)
        rbn.setPadding(25, 45, 45, 45)
        rbn.textSize = 18F
        rbn.setOnClickListener { selectOption((it as RadioButton).text.toString()) }
        detailProductOptionsGroup.addView(rbn)
    }

    private fun selectOption(selected: String) {
        TransitionManager.beginDelayedTransition(detailProductInfoCard)
        detailProductAddContainer.visibility = View.VISIBLE
        option = selected
    }
}
