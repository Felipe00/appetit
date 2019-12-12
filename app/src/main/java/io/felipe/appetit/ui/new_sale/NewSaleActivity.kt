package io.felipe.appetit.ui.new_sale

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.felipe.appetit.R
import io.felipe.appetit.database.*
import io.felipe.appetit.ui.feedback.FeedbackActivity
import io.felipe.appetit.ui.new_sale.step1.ChooseProductFragment
import io.felipe.appetit.ui.new_sale.step1.DetailProductActivity
import io.felipe.appetit.ui.new_sale.step2.ChooseClientFragment
import io.felipe.appetit.ui.new_sale.step3.FinishSaleFragment
import kotlinx.android.synthetic.main.activity_new_sale.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val PRODUCT_REQUEST = 10

class NewSaleActivity : AppCompatActivity(), ChooseProductFragment.OnFragmentInteractionListener,
    ChooseClientFragment.OnFragmentInteractionListener,
    FinishSaleFragment.OnFragmentInteractionListener {

    private lateinit var sale: Sale
    private lateinit var selectedProduct: Product
    private lateinit var chooseProductFragment: ChooseProductFragment
    private lateinit var chooseClientFragment: ChooseClientFragment
    private lateinit var finishSaleFragment: FinishSaleFragment

    companion object {
        const val PRODUCT = 1
        const val CLIENT = 2
        const val FINISH = 3
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
                if (sale.clients == null) {
                    sale.clients = ArrayList()
                    sale.clients?.add(item as Client)
                } else {
                    checkMatchOccurrences(item as Client)
                }
                chooseClientFragment.updateList(sale.clients ?: ArrayList())
            }
            FINISH -> {
                val map = item as HashMap<String, Any?>
                sale.soldAt = map["date"] as String
                sale.isPaid = map["isPaid"] as Boolean?
                sale.id = Random().nextLong()
                val database = PrefsDb.init(this@NewSaleActivity).getDatabase()
                if (database.sales == null) database.sales = ArrayList()
                database.sales?.add(sale)
                PrefsDb.init(this@NewSaleActivity).saveDatabase(database)
                startActivity(Intent(this@NewSaleActivity, FeedbackActivity::class.java))
                finish()
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

    private fun checkMatchOccurrences(client: Client) {
        val countMatchProducts =
            sale.clients?.count { it.id == client.id } ?: 0
        if (countMatchProducts > 0) {
            sale.clients?.remove(client)
        } else {
            sale.clients?.add(client)
        }
    }

    fun getProductSoldList(): List<ProductsSold> = sale.productsSold ?: emptyList()

    fun getClientList(): List<Client> = sale.clients ?: emptyList()

    fun callClientsFragment() {
        // incrementar progresso
        ObjectAnimator.ofInt(newSaleProgressBar, "progress", 2).start()
        // alterar dicas de progresso
        newSaleProgressTitle.text = getString(R.string.new_sale_progress_title_label_step_2)
        newSaleProgressStep.text = getString(R.string.new_sale_progress_step_2)
        // instanciar novo fragmento
        chooseClientFragment = ChooseClientFragment.newInstance()
        TransitionManager.beginDelayedTransition(newSaleFrameContainer)
        supportFragmentManager.beginTransaction()
            .replace(R.id.newSaleFrameContainer, chooseClientFragment).commit()
    }

    fun callFinishSaleFragment() {
        // incrementar progresso
        ObjectAnimator.ofInt(newSaleProgressBar, "progress", 3).start()
        // alterar dicas de progresso
        newSaleProgressTitle.text = getString(R.string.new_sale_progress_title_label_step_3)
        newSaleProgressStep.text = getString(R.string.new_sale_progress_step_3)
        // instanciar novo fragmento
        finishSaleFragment = FinishSaleFragment.newInstance()
        TransitionManager.beginDelayedTransition(newSaleFrameContainer)
        supportFragmentManager.beginTransaction()
            .replace(R.id.newSaleFrameContainer, finishSaleFragment).commit()
    }
}
