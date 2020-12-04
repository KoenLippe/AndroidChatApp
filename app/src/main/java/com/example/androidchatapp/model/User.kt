package com.example.androidchatapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String?, val username: String?): Parcelable {
    constructor(): this("", "") // Fix no arg constructor error
}

