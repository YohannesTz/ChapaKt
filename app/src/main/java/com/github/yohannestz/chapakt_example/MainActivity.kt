package com.github.yohannestz.chapakt_example

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
    private lateinit var secretKey: String
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        secretKey = getString(R.string.api_key_Test)
        chapaSdk = Chapa(secretKey)


        val payButton = findViewById<Button>(R.id.payBtn)
        statusTextView = findViewById(R.id.paymentStatus)

        val emailTextField = findViewById<EditText>(R.id.emailTextField)
        val nameTextField = findViewById<EditText>(R.id.nameTextField)
        val sirnameTextField = findViewById<EditText>(R.id.sirnameTextField)

        payButton.setOnClickListener {
            val postData = ChapaPostData(
                1.0,
                "ETB",
                emailTextField.text.toString(),
                nameTextField.text.toString(),
                sirnameTextField.text.toString(),
                UUID.randomUUID().toString(),
                "https://checkout.chapa.co/checkout/payment-receipt",
                "ChapaKt",
                "Chapa kotlin example",
                "https://img.icons8.com/color/344/kotlin.png"
            )
            CoroutineScope(Dispatchers.IO).launch {
                val initResult = async {
                    chapaSdk.initialize(postData)
                }
                val chapaPayment = initResult.await()
                if ((chapaPayment != null) && (chapaPayment.status == "success")) {
                    val resultIntent = Intent(applicationContext, ChapaActivity::class.java)
                    resultIntent.putExtra(
                        packageName + ChapaConstants.TRANSACTION_EXTRA_RETURN_URL,
                        postData.returnUrl
                    )
                    resultIntent.putExtra(
                        packageName + ChapaConstants.TRANSACTION_EXTRA_CHECKOUT_URL,
                        chapaPayment.data?.checkoutUrl
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

                val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
                    throwable.printStackTrace()
                }

                CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
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