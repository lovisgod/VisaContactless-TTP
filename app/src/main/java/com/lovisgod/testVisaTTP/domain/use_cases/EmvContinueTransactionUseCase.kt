package com.lovisgod.testVisaTTP.domain.use_cases

import android.content.Context
import com.lovisgod.testVisaTTP.domain.SoftposRepository
import com.lovisgod.testVisaTTP.handlers.HorizonPayException

class EmvContinueTransactionUseCase (private val repository: SoftposRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(condition: Boolean, context: Context){
        return repository.continueTransaction(condition, context)
    }
}