package com.lovisgod.testVisaTTP

import android.app.Application
import android.nfc.NfcAdapter
import android.os.Bundle

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton


import com.lovisgod.testVisaTTP.SDKHelper.nfcListener

import com.lovisgod.testVisaTTP.handlers.network.networkInterface.networkSampleRepo
import com.lovisgod.testVisaTTP.models.OnlineRespEntity
import com.lovisgod.testVisaTTP.models.datas.RequestIccData
import com.lovisgod.testVisaTTP.models.datas.TerminalInfo
import com.lovisgod.testVisaTTP.models.enums.AccountType
import com.lovisgod.testVisaTTP.models.enums.KeyMode
import com.lovisgod.testVisaTTP.models.enums.TransactionResultCode
import com.lovisgod.testVisaTTP.models.uiState.ReadCardStates
import com.visa.app.ttpkernel.ContactlessConfiguration
import com.visa.app.ttpkernel.ContactlessKernel
import kotlinx.coroutines.runBlocking




class MainActivity : AppCompatActivity(), TransactionLogger, ReadCardStates {

    private var nfcAdapter: NfcAdapter? = null
    var contactlessConfiguration: ContactlessConfiguration? = null
    var contactlessKernel: ContactlessKernel? = null
    var mainLog: TextView? = null
    var pintext: TextView? = null
    var testBtn: AppCompatButton? = null
    var view: View?  = null

    val useCases = SoftApplication.container.softposAppContainer.getUseCases()


    // Supported AIDs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLog = findViewById(R.id.mainLog)
        testBtn = findViewById(R.id.testBtn)
        view = findViewById(R.id.keypad_layout)
        pintext = findViewById(R.id.pinText)


        setupNfc()



        SDKHelper.setPinMode(KeyMode.ISO_0) // change to dukpt when testing dukpt


        doDummyPinKey() //this is dummy pinkey loading for the purpose testing pinkey injection


        testBtn?.setOnClickListener {
           runBlocking {
               useCases.setTerminalConfigUseCase.invoke(TerminalInfo(
                   terminalCapabilities = "E0F8C8",
                   terminalCountryCode = "0566",
                   transCurrencyCode = "0566",
                   cardAcceptorNameLocation = "ISW Visa sample    Oko Awo Street"
               ))
                useCases.emvSetIsKimonoUseCase.invoke(false)
                useCases.emvPayUseCase.invoke("100".toInt().toLong(), this@MainActivity, this@MainActivity.applicationContext)

           }
        }

    }

    fun setupNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        SDKHelper.initialize(this.applicationContext, nfcAdapter!!)
    }



    override fun log(message: String) {
        println(message)
    }


    override fun onResume() {
        println(nfcListener == null)
        super.onResume()

        val NFC_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS
        nfcListener!!.let {
            nfcAdapter!!.enableReaderMode(this,
                it::onNfcTagDiscovered, NFC_FLAGS, null)
        }
    }

    override fun onPause() {
        println("on pause called")
        super.onPause()
        nfcListener?.resetNFCField()
        nfcAdapter!!.disableReaderMode(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        nfcListener?.resetNFCField()
    }

    fun doDummyPinKey() {
        runBlocking {
            networkSampleRepo.downloadKeys(this@MainActivity.applicationContext)
        }
    }

    override fun onInsertCard() {
        println("Insert card called")
    }

    override fun onCardRead(cardType: String, cardNo: String) {
        super.onCardRead(cardType, cardNo)
       runBlocking {
           useCases.continueTransactionUseCase.invoke(true, this@MainActivity)
       }
    }

    override fun onRemoveCard() {
        println("Card removed")
    }

    override fun onPinInput() {
        println("Input pin")
    }

    override fun sendTransactionOnline(emvData: RequestIccData): OnlineRespEntity {
       val response = runBlocking {
           pintext?.text = ""
           val response  = networkSampleRepo.testTrans(this@MainActivity.applicationContext, emvData)
           return@runBlocking response
       }

      return OnlineRespEntity()
    }

    override fun onEmvProcessing(message: String) {
        println("emv processing")
    }

    override fun onEmvProcessed(data: Any?, code: TransactionResultCode) {
        println(data)
        println(code.name)
    }

    override fun onSelectAccountType(): AccountType {
       return AccountType.Default
    }

    override fun onPinText(text: String) {
        super.onPinText(text)
        pintext?.text = text
    }


    override fun onTransactionFailed(reason: String) {
        super.onTransactionFailed(reason)
    }
}

class SampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SoftApplication.onCreate(this.applicationContext)
    }
}