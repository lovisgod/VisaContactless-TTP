package com.lovisgod.testVisaTTP.domain.use_cases

import android.graphics.Bitmap
import com.lovisgod.testVisaTTP.domain.SoftposRepository
import com.lovisgod.iswhpay.ui.uiState.PrintingState

class PrintBitMapUseCase(private val repository: SoftposRepository) {

    suspend operator fun invoke(bitmap: Bitmap, printingState: PrintingState){
        return repository.printBitMap(bitmap, printingState)
    }
}
