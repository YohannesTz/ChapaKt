package com.github.yohannestz.chapakt.chapa.util

import com.github.yohannestz.chapakt.chapa.models.ChapaPostData

class ChapaValidator {
    companion object {
        fun isValid(postData: ChapaPostData): Boolean {
            var isValid = true
            if (postData.firstName.trim().isEmpty()) {
                isValid = false
                throw ChapaException(ChapaConstants.firstNameRequired)
            }
            if (postData.lastName.trim().isEmpty()) {
                isValid = false
                throw ChapaException(ChapaConstants.lastNameRequired)
            }
            if (postData.txRef.trim().isEmpty()) {
                isValid = false
                throw ChapaException(ChapaConstants.transactionReferenceRequired)
            }
            if (postData.email.trim().isEmpty()) {
                isValid = false
                throw ChapaException(ChapaConstants.emailRequired)
            }
            if (postData.currency.trim().isEmpty()) {
                isValid = false
                throw ChapaException(ChapaConstants.currencyRequired)
            }
            if (postData.amount <= 0) {
                isValid = false
                throw ChapaException(ChapaConstants.amountRequired)
            }
            return isValid
        }
    }
}
