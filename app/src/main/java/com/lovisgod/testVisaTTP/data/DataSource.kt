package com.lovisgod.testVisaTTP.data

import android.content.Context
import android.graphics.Bitmap
import com.lovisgod.iswhpay.ui.uiState.PrintingState
import com.lovisgod.testVisaTTP.MainActivity
import com.lovisgod.testVisaTTP.models.datas.IswHpCodes
import com.lovisgod.testVisaTTP.models.datas.TerminalInfo
import com.lovisgod.testVisaTTP.models.uiState.ReadCardStates

class DataSource(val emvDataKeyManager: EmvDataKeyManager, val emvPaymentHandler: EmvPaymentHandler) {



    suspend fun setEmvParameter(terminalInfo: TerminalInfo): Int {
        return try {
            emvDataKeyManager.setEmvConfig(terminalInfo)
            IswHpCodes.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun setPinKey(isDukpt: Boolean = true, key: String = "", ksn: String = ""): Int {
        return try {
            emvDataKeyManager.setPinKey(isDukpt, key, ksn)
        } catch (e:Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun pay(amount: Long, readCardStates: ReadCardStates, context: Context) {
        try {
            emvPaymentHandler.pay(amount.toInt().toString(), readCardStates, context)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun setIsKimono(isKimono: Boolean){
        try {
            emvPaymentHandler.setIsKimono(isKimono)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }


    suspend fun continueTransaction(condition: Boolean, activity: Context) {
        try {
            emvPaymentHandler.continueTransaction(condition, activity)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun stopTransaction() {
        try {
            emvPaymentHandler.stopTransaction()
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }
    suspend fun printBitMap(bitmap: Bitmap, printingState: PrintingState) {
        try {
//            emvPaymentHandler.handlePrinting(bitmap, printingState)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }
}