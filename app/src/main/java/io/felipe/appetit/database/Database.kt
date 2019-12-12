package io.felipe.appetit.database

import com.google.gson.annotations.SerializedName
import java.text.NumberFormat

class Database {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("user")
    var user: User? = null
    @SerializedName("clients")
    var clients: List<Client>? = null
    @SerializedName("sales")
    var sales: ArrayList<Sale>? = null
    @SerializedName("products")
    private var products: List<Product>? = null
    @SerializedName("is_logged_in")
    var isLoggedIn: Boolean? = null
    val sortedProducts: List<Product>?
        get() {
            var currentCategory = ""
            return this.products?.map {
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
            }
        }
}

class Client {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
    var isSelected: Boolean? = null
}

class Product {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("category")
    var category: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("price")
    var price: Int? = null
    @SerializedName("options")
    var options: List<String>? = null
    var isSelected: Boolean? = null
    var isSection: Boolean? = null

    fun bindPrice(): String? {
        return NumberFormat.getCurrencyInstance().format((price ?: 0).toDouble() / 100)
    }
}

data class ProductsSold(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("id_product")
    var idProduct: Long? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("price")
    var price: Int? = null,
    @SerializedName("option")
    var option: String? = null,
    @SerializedName("comments")
    var comments: String? = null,
    @SerializedName("quantity")
    var quantity: Int? = null
)

class Sale {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("products_sold")
    var productsSold: ArrayList<ProductsSold>? = null
    @SerializedName("soldAt")
    var soldAt: String? = null
    @SerializedName("clients")
    var clients: ArrayList<Client>? = null
    @SerializedName("isPaid")
    var isPaid: Boolean? = null

    fun bindClients(): String? {
        return clients?.joinToString { "${it.name}" }
    }

    fun bindPrice(): String? {
        return NumberFormat.getCurrencyInstance()
            .format(productsSold?.sumByDouble {
                ((it.price ?: 0) * (it.quantity ?: 0)).toDouble() / 100.00
            })
    }

    fun bindProducts(): String? {
        return productsSold?.joinToString { "${it.quantity}x ${it.name}" }
    }
}

class User {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("company")
    var company: Long? = null
}