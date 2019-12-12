package io.felipe.appetit.ui.new_sale

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import io.felipe.appetit.R
import io.felipe.appetit.database.Product
import io.felipe.appetit.database.ProductsSold
import io.felipe.appetit.database.Sale
import io.felipe.appetit.ui.new_sale.step1.ChooseProductFragment
import io.felipe.appetit.ui.new_sale.step1.DetailProductActivity
import kotlinx.android.synthetic.main.activity_new_sale.*

const val PRODUCT_REQUEST = 10

class NewSaleActivity : AppCompatActivity(), ChooseProductFragment.OnFragmentInteractionListener {

    private lateinit var sale: Sale
    private lateinit var selectedProduct: Product
    private lateinit var chooseProductFragment: ChooseProductFragment

    companion object {
        const val PRODUCT = 1
        const val CLIENT = 2
        const val DATE = 3
    }

    override fun onFragmentInteraction(item: Any, type: Int) {
        when (type) {
            PRODUCT -> {
                selectedProduct = item as Product
                startActivityForResult(
                    Intent(
                        this@NewSaleActivity,
                        DetailProductActivity::class.java
                    ).putExtra("product", Gson().toJson(selectedProduct)), PRODUCT_REQUEST
                )
            }
            CLIENT -> {
            }
            DATE -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_sale)

        if (savedInstanceState == null) {
            chooseProductFragment = ChooseProductFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.newSaleFrameContainer,
                    chooseProductFragment,
                    "chooseProduct"
                )
                .commit()
            sale = Sale()
        }

        newSaleBack.setOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PRODUCT_REQUEST -> {
                    val productsSold = Gson().fromJson(
                        data?.getStringExtra("productSold"),
                        ProductsSold::class.java
                    )
                    if (sale.productsSold == null) {
                        sale.productsSold = ArrayList()
                        sale.productsSold?.add(productsSold)
                    } else {
                        checkMatchOccurrences(productsSold)
                    }
                    chooseProductFragment.updateList(productsSold)
                }
            }
        }
    }

    private fun checkMatchOccurrences(productsSold: ProductsSold) {
        val countMatchProducts =
            sale.productsSold?.count { it.idProduct == productsSold.idProduct } ?: 0
        if (countMatchProducts > 0) {
            sale.productsSold?.map {
                if (it.idProduct == productsSold.idProduct) {
                    it.quantity = productsSold.quantity
                    it.option = productsSold.option
                    it.comments = productsSold.comments
                }
                it
            }
        } else {
            sale.productsSold?.add(productsSold)
        }
    }

    fun getProductSoldList(): List<ProductsSold> = sale.productsSold ?: emptyList()

    fun callClientsFragment() {
        supportFragmentManager.inTransaction {
            // add(newSaleFrameContainer, )
        }
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}
