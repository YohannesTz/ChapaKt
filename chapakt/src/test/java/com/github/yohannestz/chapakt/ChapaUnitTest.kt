package com.github.yohannestz.chapakt

import com.github.yohannestz.chapakt.chapa.models.ChapaPostData
import com.github.yohannestz.chapakt.chapa.Chapa
import com.github.yohannestz.chapakt.chapa.models.SubAccount
import com.github.yohannestz.chapakt.chapa.models.Transfer
import com.github.yohannestz.chapakt.chapa.util.SplitType
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ChapaUnitTest {
    private val chapa = Chapa("CHASECK_TEST-MAhZcQXEHgi1Giji0kq9t3ruHsMZu5qd")
    private val generatedTxRef = chapa.getFormattedTxRef()

    @Test
    fun `initialize payment`() = runBlocking {
        val payment = chapa.initialize(
            ChapaPostData(
                10.0,
                "ETB",
                "yohannes222ethiopia@gmail.com",
                "Yohannes",
                "Tezera",
                generatedTxRef,
                "https://checkout.chapa.co/checkout/payment-receipt",
                "ChapaKt", //optional
                "Chapa kotlin example", //optional
                "SomeLogo"
            )
        )
        assertNotNull(payment)
        assertNotNull(payment!!.data!!.checkoutUrl)
    }

    @Test
    fun `initialize payment then verify transaction`() = runBlocking {
        val verifyTransaction = chapa.verifyTransaction(generatedTxRef)
        assertNotNull(verifyTransaction)
    }

    @Test
    fun `get list of banks supported`() = runBlocking {
        val listOfBanks = chapa.getListOfBanks()
        assertFalse(listOfBanks.isEmpty())
        assertNotNull(listOfBanks)
    }

    @Test
    fun `create subaccount with flat split`() = runBlocking {
        val createSubAccount = chapa.createSubAccount(
            SubAccount(
                "Chapa souq",
                "80a510ea-7497-4499-8b49-ac13a3ab7d07",
                "Chapa Souq Enterprise",
                "00000",
                SplitType.FLAT.value,
                10.0
            )
        )
        assertNotNull(createSubAccount)
    }

    @Test
    fun `create subaccount with percentage split`() = runBlocking {
        val createSubAccount = chapa.createSubAccount(
            SubAccount(
                "Chapa souq",
                "80a510ea-7497-4499-8b49-ac13a3ab7d07",
                "Chapa Souq Enterprise",
                "00000",
                SplitType.PERCENTAGE.value,
                10.0
            )
        )
        assertNotNull(createSubAccount)
    }

    @Test
    fun `initialize transfer`() = runBlocking {
        val initializeTransfer = chapa.initTransfer(
            Transfer(
                "Abebe",
                "0000",
                "Abebe beneficiary",
                100.0,
                "ETB",
                chapa.getFormattedTxRef(),
                "80a510ea-7497-4499-8b49-ac13a3ab7d07"
            )
        )

        assertNotNull(initializeTransfer)
    }

    @Test
    fun `get formatted transaction reference`() {
        val formattedTxRef = chapa.getFormattedTxRef()
        assertFalse(formattedTxRef.isEmpty())
    }
}