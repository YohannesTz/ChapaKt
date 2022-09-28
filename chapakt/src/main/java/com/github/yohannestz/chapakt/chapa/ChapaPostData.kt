package com.github.yohannestz.chapakt.chapa

data class ChapaPostData (
    var amount: Double,
    var currency: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var txRef: String,
    var returnUrl: String,
    var customizationTitle: String = "",
    var customizationDescription: String = "",
    var customizationLogo: String = ""
)