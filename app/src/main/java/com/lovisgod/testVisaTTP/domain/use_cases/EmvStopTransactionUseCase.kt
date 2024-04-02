package com.lovisgod.testVisaTTP.domain.use_cases

import com.lovisgod.testVisaTTP.domain.SoftposRepository
import com.lovisgod.testVisaTTP.handlers.HorizonPayException

class EmvStopTransactionUseCase (private val repository: SoftposRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(){
        return repository.stopTransaction()
    }
}