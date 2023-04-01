package com.github.yohannestz.chapakt.chapa.models

data class ChapaPostData(
    var amount: Double = 0.01,
    var currency: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var txRef: String,
    var returnUrl: String,
    var customizationTitle: String = "",
    var customizationDescription: String = "",
    var customizationLogo: String = "",
    var subAccountId: String = ""
)