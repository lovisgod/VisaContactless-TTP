package com.lovisgod.testVisaTTP.domain.use_cases

data class AllUseCases(
   val downloadAid: DownloadAidUseCase,
   val downloadCapkUseCase: DownloadCapkUseCase,
   val setTerminalConfigUseCase: SetTerminalConfigUseCase,
   val setPinKeyUseCase: SetPinKeyUseCase,
   val emvPayUseCase: EmvPayUseCase,
   val printBitMapUseCase: PrintBitMapUseCase,
   val continueTransactionUseCase: EmvContinueTransactionUseCase,
   val emvSetIsKimonoUseCase: EmvSetIsKimonoUseCase,
   val stopTransactionUseCase: EmvStopTransactionUseCase
)