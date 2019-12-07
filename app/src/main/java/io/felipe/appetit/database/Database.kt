package io.felipe.appetit.database

import com.google.gson.annotations.SerializedName

class Database {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("users")
    var users: List<User>? = null
    @SerializedName("clients")
    var clients: List<Client>? = null
    @SerializedName("sales")
    var sales: List<Sale>? = null
    @SerializedName("products")
    var products: List<Product>? = null

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

}

class ProductsSold {

    @SerializedName("id")
    var id: Long? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("price")
    var price: String? = null
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
    @SerializedName("is_logged_in")
    var isLoggedIn: Boolean? = null
}