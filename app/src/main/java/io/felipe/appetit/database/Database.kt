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
    var sales: List<Sale>? = null
    @SerializedName("products")
    var products: List<Product>? = null
    @SerializedName("is_logged_in")
    var isLoggedIn: Boolean? = null
}

class Client {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
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

    fun bindPrice(): String? {
        return "R$ " + (price ?: 0 / 100)
    }
}

class ProductsSold {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("price")
    var price: Int? = null
    @SerializedName("option")
    var option: String? = null
    @SerializedName("comments")
    var comments: String? = null
    @SerializedName("quantity")
    var quantity: Int? = null
}

class Sale {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("products_sold")
    var productsSold: List<ProductsSold>? = null
    @SerializedName("soldAt")
    var soldAt: String? = null
    @SerializedName("clients")
    var clients: List<Client>? = null
    @SerializedName("isPaid")
    var isPaid: Boolean? = null

    fun bindClients(): String? {
        return clients?.joinToString { "${it.name}" }
    }

    fun bindPrice(): String? {
        return NumberFormat.getCurrencyInstance()
            .format(productsSold?.sumBy { it.price ?: 0 / 100 }!!.toDouble() / 100)
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