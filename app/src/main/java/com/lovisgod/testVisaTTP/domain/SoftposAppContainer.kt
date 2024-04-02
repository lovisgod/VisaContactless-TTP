package com.lovisgod.testVisaTTP.domain

import com.lovisgod.testVisaTTP.data.DataSource
import com.lovisgod.testVisaTTP.data.EmvDataKeyManager
import com.lovisgod.testVisaTTP.data.EmvPaymentHandler
import com.lovisgod.testVisaTTP.domain.use_cases.AllUseCases
import com.lovisgod.testVisaTTP.domain.use_cases.DownloadAidUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.DownloadCapkUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.EmvContinueTransactionUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.EmvPayUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.EmvSetIsKimonoUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.EmvStopTransactionUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.PrintBitMapUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.SetPinKeyUseCase
import com.lovisgod.testVisaTTP.domain.use_cases.SetTerminalConfigUseCase

class SoftposAppContainer {

    val emvDataKeyManager = EmvDataKeyManager()
    val emvPaymentHandler = EmvPaymentHandler.getInstance()
    private val dataSource = DataSource(emvDataKeyManager, emvPaymentHandler)
    private val repository = SoftposRepository(dataSource)


    fun getUseCases(): AllUseCases {
        println("this got called")
         return AllUseCases(
             downloadAid = DownloadAidUseCase(repository),
             downloadCapkUseCase = DownloadCapkUseCase(repository),
             setTerminalConfigUseCase = SetTerminalConfigUseCase(repository),
             setPinKeyUseCase = SetPinKeyUseCase(repository),
             emvPayUseCase = EmvPayUseCase(repository),
             printBitMapUseCase = PrintBitMapUseCase(repository),
             continueTransactionUseCase = EmvContinueTransactionUseCase(repository),
             emvSetIsKimonoUseCase = EmvSetIsKimonoUseCase(repository),
             stopTransactionUseCase = EmvStopTransactionUseCase(repository)
         )
    }

//    fun initializeEmvDataManager()

}