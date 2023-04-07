package com.github.yohannestz.chapakt.chapa.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.github.yohannestz.chapakt.R
import com.github.yohannestz.chapakt.chapa.Chapa
import com.github.yohannestz.chapakt.chapa.util.ChapaConstants

class ChapaActivity : AppCompatActivity() {
    private lateinit var webViewLayer: LinearLayout
    private lateinit var verificationLayer: LinearLayout
    private lateinit var lottieView: LottieAnimationView
    private lateinit var chapaSdk: Chapa

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapa)

        val returnUrl = intent.getStringExtra(packageName + ChapaConstants.TRANSACTION_EXTRA_RETURN_URL)
        val checkOutUrl = intent.getStringExtra(packageName + ChapaConstants.TRANSACTION_EXTRA_CHECKOUT_URL)
        val txRef = intent.getStringExtra(packageName + ChapaConstants.TRANSACTION_EXTRA_TX_REF)

        webViewLayer = findViewById(R.id.webViewLayer)
        verificationLayer = findViewById(R.id.verificationLayer)
        val checkOutWebView = findViewById<WebView>(R.id.checkOutWebView)
        lottieView = findViewById(R.id.lottieView)
        chapaSdk = Chapa(intent.getStringExtra("secreteKey").toString())

        checkOutWebView.webViewClient = ChapaBrowser(returnUrl.toString(), txRef.toString(), this)
        checkOutWebView.settings.loadsImagesAutomatically = true
        checkOutWebView.settings.javaScriptEnabled = true
        checkOutWebView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        checkOutWebView.clearCache(true)
        checkOutWebView.addJavascriptInterface(ChapaWebInterface(this), "ChapaAndroid")
        checkOutWebView.loadUrl(checkOutUrl.toString())
    }

    class ChapaWebInterface(private val context: Context) {
        @JavascriptInterface
        fun cancelClicked() {
            Toast.makeText(context, "Cancel Clicked", Toast.LENGTH_LONG).show()
        }
    }

    inner class ChapaBrowser(private val returnUrl: String, private val txRef: String, private val context: Context): WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            view?.loadUrl("javascript:(function cancelClicked(){" +
                    "alert(\"This was cool\")" +
                    "})")
            /*view?.loadUrl("<script type=\"text/javascript\">" +
                    "function cancelClicked() {" +
                        "ChapaAndroid.cancelClicked();" +
                    "}" +
                    "</script>"
            )*/
            verificationLayer.visibility = View.INVISIBLE
            webViewLayer.visibility = View.VISIBLE
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            if (url!!.contains(returnUrl)) {
                Toast.makeText(context, "Payment Finished!", Toast.LENGTH_LONG).show()
                webViewLayer.visibility = View.GONE
                verificationLayer.visibility = View.VISIBLE
                val data = Intent()
                data.putExtra("status", "success")
                data.putExtra(packageName + ChapaConstants.TRANSACTION_EXTRA_TX_REF, txRef)
                setResult(RESULT_OK, data)
                finish()
            }
            super.doUpdateVisitedHistory(view, url, isReload)
        }

    }
}