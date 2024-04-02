package com.lovisgod.testVisaTTP.domain.use_cases

import com.lovisgod.testVisaTTP.domain.SoftposRepository

import com.lovisgod.testVisaTTP.handlers.HorizonPayException
import com.lovisgod.testVisaTTP.models.datas.TerminalInfo

class SetTerminalConfigUseCase(private val repository: SoftposRepository) {
    @Throws(HorizonPayException::class)
    suspend operator fun invoke(terminalInfo: TerminalInfo): Int{
        return repository.setTerminalConfig(terminalInfo)
    }
}