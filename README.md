# ChapaKt (Unofficial)
![Code Grade](https://api.codiga.io/project/34815/status/svg)

Simple api wrapper and utilities for chapa payment gateway. I know this is a hacky or not a really
good solution but that's all what we got now until chapa releases an official Android-Sdk or just a
rest api for everybody to use.

# Api Reference

So, let's get started.
The [official chapa documentation](https://developer.chapa.co/docs/accept-payments/) states before
carrying out the transaction, a user must provide required information such as full name, email
address, the amount to transfer, etc. Below you will find a list of parameter needed:

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `key`      | `string` | **
Required**. This will be your public key from Chapa. When on test mode use the test key, and when on live mode use the live key. |
| `email`    | `string` | **Required**. A customer’s email. address. |
| `amount`   | `string` | **Required**. The amount you will be charging your customer. |
| `first_name` | `string` | **Required**. A customer’s first name. |
| `last_name`      | `string` | **Required**. Acustomer's last name. |
| `tx_ref`   | `string` | **Required**. A unique reference given to each transaction. |
| `currency` | `string` | **
Required**. The currency in which all the charges are made. i.e ETB, USD |
| `callback_url`| `string` |  The URL to redirect the customer to after payment is done.|
| `customization[title]`| `string` |  The customizations field (optional) allows you to customize the look and feel of the payment modal.|

**note:-** Since we are using redirections to check if a payment is finished ***return_url*** is a
must in this library. because with out it we don't have any way of checking the current url.

# Heads Up!

We use kotlin coroutines in the project and example to handle all the requests and as such so I
would recommend you to read
[kotlin-croutines](https://kotlinlang.org/docs/coroutines-overview.html) from the docs if you don't
know them already. as for java developers I didn't provide any thing because I think its been a
while since I have done Android development in java.

Also, I do not consider my self as an expert in kotlin so if you find anything that can be Improved
please open an issue, pullrequest or just contact me. any amount of help is appreciated.

# The api

In need of another solution? checkout [chapa-dart](https://github.com/Chapa-Et/chapa-flutter)
or [chapa-java](https://github.com/Chapa-Et/chapa-java)

## Installation

First add jitpack to your project

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```groovy
dependencies {
    implementation 'com.github.YohannesTz:ChapaKt:1.0.0'
}
```

###

## Usage/Example

first we need to initialize the sdk by giving it your secretKey

```kotlin
  private val secretKey = "you-chapa-secrete-key"
  private lateinit var chapaSdk: Chapa
```

and then inside **OnCreate** method

```
  chapaSdk = Chapa(secretKey)
```

then we will create a chapa post data with the required variables

```kotlin
  val postData = ChapaPostData(
    55.0,
    "ETB",
    "abebe@bikila.com",
    "Abebe",
    "Bikila",
    UUID.randomUUID().toString(),
    "https://checkout.chapa.co/checkout/payment-receipt",
    "ChapaKt", //optional
    "Chapa kotlin example", //optional
    "SomeLogo" //optional
  )
```

After initializing the post data when we want to trigger the payment (let's say with button click)
what we have to do it fist initialize a payment object using the **chapa** class then after you can
launch the **ChapaActivity**. The activity expects you to pass ***return_url**, ***checkout_url***
, ***tx_ref***, ***secretKey***
on the intent as an extra.

*please don't pass the bare elements to ChapaActivity because you could have two or more apps using
simultaneously and add your package name just to be safe*

```kotlin
  payButton.setOnClickListener {
    CoroutineScope(Dispatchers.IO).launch {
        val initResult = async {
            chapaSdk.initialize(postData)
        }
        val chapaPayment = initResult.await()
        if (chapaPayment != null && chapaPayment.status == "success") {
            val resultIntent = Intent(applicationContext, ChapaActivity::class.java)
            resultIntent.putExtra(
                packageName + ChapaConstants.TRANSACTION_EXTRA_RETURN_URL,
                postData.returnUrl
            )
            resultIntent.putExtra(
                packageName + ChapaConstants.TRANSACTION_EXTRA_CHECKOUT_URL,
                chapaPayment.data.checkoutUrl
            )
            resultIntent.putExtra(
                packageName + ChapaConstants.TRANSACTION_EXTRA_TX_REF,
                postData.txRef
            )
            resultIntent.putExtra(
                "secreteKey",
                secretKey
            )
            paymentActivityLauncher.launch(resultIntent)
        }
    }
  }
```

after that you have to launch your activity but you need to get the result from it. so
traditionally, this could be achived by *startActivityForResult* but since that api is deprecated
long ago we will be using the activity lanucher. after the Activity returned the payment response is
attached with the result intent. you can get the response object by doing *getStringExtra(
packageName + ChapaConstants.TRANSACTION_EXTRA_TX_REF)* after checking the results you can update
your ui based on the response.

```kotlin
  private var paymentActivityLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch {
                val txRef =
                    result.data!!.getStringExtra(packageName + ChapaConstants.TRANSACTION_EXTRA_TX_REF)
                if (txRef != null) {
                    val trRes: ChapaResponse? = withContext(Dispatchers.Default) {
                        chapaSdk.verifyPayment(txRef)
                    }
                    if (trRes != null && trRes.status == "success") { // payment successfull, update ui here
                        this@MainActivity.runOnUiThread {
                            statusTextView.text = "Paid!"
                        }
                    }
                } else {
                    this@MainActivity.runOnUiThread {
                        statusTextView.text = "Payment failed!"
                    }
                }
            }
        }
    }
```

## todo

- Better handling of background tasks
- Translations
