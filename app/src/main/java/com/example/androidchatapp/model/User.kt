package com.example.androidchatapp.model

data class User(val uid: String?, val username: String?) {
    constructor(): this("", "") // Fix no arg constructor error
}

