package com.github.yohannestz.chapakt.chapa.util

object ChapaConstants {
    //urls
    const val baseMobileUrl = "https://api.chapa.co/v1/transaction/mobile-initialize"

    const val baseUrl = "https://api.chapa.co/v1/transaction/initialize"
    const val chargeCardUrl = "charges?type=card"
    const val validateCharge = "validate-charge"
    const val defaultRedirectUrl = "https://chapa.co/"
    const val verifyTransaction = "https://api.chapa.co/v1/transaction/verify/"
    const val getSupportedBanksUrl = "https://api.chapa.co/v1/banks"
    const val subAccountUrl = "https://api.chapa.co/v1/subaccount"
    const val initiateTransfer = "https://api.chapa.co/v1/transfers"

    //messages
    const val publicKeyRequired = "Public key is required"

    const val currencyRequired = "Currency is required"
    const val amountRequired = "Amount is required";
    const val emailRequired = "Email is required";
    const val firstNameRequired = "First Name is required";
    const val lastNameRequired = "First Name is required";
    const val transactionReferenceRequired = "Transaction is required";
    const val connectionError="Connectivity Issue";
    //Intent Extra
    const val TRANSACTION_EXTRA_RETURN_URL = "RETURN_URL"
    const val TRANSACTION_EXTRA_CHECKOUT_URL = "CHECKOUT_URL"
    const val TRANSACTION_EXTRA_TX_REF = "TX_REF"
    const val TRANSACTION_EXTRA_PAYMENT_RESULT = "PAYMENT_RESULT"
}
