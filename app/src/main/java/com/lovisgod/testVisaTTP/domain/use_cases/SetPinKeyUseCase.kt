package com.lovisgod.testVisaTTP.domain.use_cases

import com.lovisgod.testVisaTTP.domain.SoftposRepository
import com.lovisgod.testVisaTTP.handlers.HorizonPayException

class SetPinKeyUseCase(val repository: SoftposRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(isDukpt: Boolean = true, key: String, ksn: String): Int{
        return repository.setPinKey(isDukpt, key, ksn)
    }
}