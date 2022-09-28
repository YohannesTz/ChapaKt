package com.github.yohannestz.chapakt_example

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.github.yohannestz.chapakt.ChapaActivity
import com.github.yohannestz.chapakt.chapa.Chapa
import com.github.yohannestz.chapakt.chapa.ChapaConstants
import com.github.yohannestz.chapakt.chapa.ChapaPostData
import com.github.yohannestz.chapakt.chapa.ChapaResponse
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var chapaSdk: Chapa
    private val secretKey = "CHASECK_TEST-nmC3d1jQDGzFxngu38xtSVD4QYRU3yWJ"
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chapaSdk = Chapa(secretKey)

        val postData = ChapaPostData(
            55.0,
            "ETB",
            "abebe@bikila.com",
            "Abebe",
            "Bikila",
            UUID.randomUUID().toString(),
            "https://checkout.chapa.co/checkout/payment-receipt",
            "ChapaKt",
            "Chapa kotlin example",
            "SomeLogo"
        )

        val payButton = findViewById<Button>(R.id.payBtn)
        statusTextView = findViewById(R.id.paymentStatus)

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
    }

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
                        if (trRes != null && trRes.status == "success") {
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
}