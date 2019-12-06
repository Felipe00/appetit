package io.felipe.appetit.database

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Database {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("users")
    @Expose
    var users: List<User>? = null
    @SerializedName("clients")
    @Expose
    var clients: List<Client>? = null
    @SerializedName("sales")
    @Expose
    var sales: List<Sale>? = null
    @SerializedName("products")
    @Expose
    var products: List<Product>? = null

}

class Client {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}

class Product {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("price")
    @Expose
    var price: Int? = null
    @SerializedName("options")
    @Expose
    var options: List<String>? = null

}

class ProductsSold {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("option")
    @Expose
    var option: String? = null
    @SerializedName("comments")
    @Expose
    var comments: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Int? = null

}

class Sale {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("products_sold")
    @Expose
    var productsSold: List<ProductsSold>? = null
    @SerializedName("soldAt")
    @Expose
    var soldAt: String? = null
    @SerializedName("clients")
    @Expose
    var clients: List<Client>? = null
    @SerializedName("isPaid")
    @Expose
    var isPaid: Boolean? = null

}

class User {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("company")
    @Expose
    var company: Long? = null

}