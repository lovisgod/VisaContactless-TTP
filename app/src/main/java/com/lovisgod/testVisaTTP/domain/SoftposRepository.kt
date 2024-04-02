package com.lovisgod.testVisaTTP.domain

import android.content.Context
import android.graphics.Bitmap
import com.lovisgod.testVisaTTP.data.DataSource
import com.lovisgod.iswhpay.ui.uiState.PrintingState
import com.lovisgod.testVisaTTP.models.datas.TerminalInfo
import com.lovisgod.testVisaTTP.models.uiState.ReadCardStates
//import com.lovisgod.iswhpay.utils.models.TerminalInfo

class SoftposRepository(val dataSource: DataSource) {
    suspend fun downloadAid() = 0
    suspend fun dowloadCapk() = 0
    suspend fun setTerminalConfig(terminalInfo: TerminalInfo) = dataSource.setEmvParameter(terminalInfo)
    suspend fun setPinKey(
        isDukpt: Boolean = true, key: String = "", ksn: String = "") = dataSource.setPinKey(isDukpt, key, ksn)
    suspend fun pay(amount: Long, readCardStates: ReadCardStates, context: Context) = dataSource.pay(amount, readCardStates, context)

    suspend fun continueTransaction(condition: Boolean, context: Context) = dataSource.continueTransaction(condition, context)

    suspend fun stopTransaction() = dataSource.stopTransaction()


    suspend fun setIsKimono(isKimono: Boolean) = dataSource.setIsKimono(isKimono)
    suspend fun printBitMap(bitmap: Bitmap, printingState: PrintingState) = dataSource.printBitMap(bitmap, printingState)
}