package io.felipe.appetit.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.felipe.appetit.R
import io.felipe.appetit.database.PrefsDb
import io.felipe.appetit.database.Sale
import io.felipe.appetit.ui.new_sale.NewSaleActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var saleAdapter: ListSaleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardNewSale.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewSaleActivity::class.java))
        }

        mainSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                peformSearch(s.toString())
            }
        })
    }

    private fun peformSearch(text: String) {
        val db = PrefsDb.init(this@MainActivity).getDatabase()
        if (text.isEmpty()) {
            saleAdapter.update(db.sales?.sortedByDescending { it.soldAt })
            return
        }
        if (text.isEmpty()) {
            if (::saleAdapter.isInitialized) {
                saleAdapter.update(db.sales)
            }
            return
        }
        // filtrando listas de pedidos pelo nome do cliente
        val salesByClients = ArrayList(db.sales?.filter {
            it.clients?.filter { c ->
                c.name?.toUpperCase(Locale.getDefault())?.contains(
                    text.toUpperCase(
                        Locale.getDefault()
                    )
                ) == true
            }?.size ?: 0 > 0
        } ?: emptyList())
        // filtrando listas de pedidos pelo nome do produto
        val salesByProducts = ArrayList(db.sales?.filter {
            it.productsSold?.filter { p ->
                p.name?.toUpperCase(Locale.getDefault())?.contains(
                    text.toUpperCase(
                        Locale.getDefault()
                    )
                ) == true
            }?.size ?: 0 > 0
        } ?: emptyList())

        val sumSales = ArrayList<Sale>()
        sumSales.addAll(salesByClients)
        sumSales.addAll(salesByProducts)

        saleAdapter.update(sumSales.distinct().sortedByDescending { it.soldAt })
    }

    override fun onResume() {
        super.onResume()

        val database = PrefsDb.init(this).getDatabase()
        mainTitle.text = database.user?.name ?: "Não identificado"
        with(mainSaleList) {

            saleAdapter =
                ListSaleAdapter(database.sales?.asReversed()?.sortedByDescending { it.soldAt }
                    ?: emptyList()) {
                    // TODO Em um futuro próximo: ao clicar, abrir uma tela de detalhes aqui
                }
            adapter = saleAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}

