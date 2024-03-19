package com.lovisgod.testVisaTTP.data

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.RemoteException
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.isw.pinencrypter.Converter.GetPinBlock
import com.lovisgod.nfcpos.utils.BerTag
import com.lovisgod.testVisaTTP.SDKHelper
import com.lovisgod.testVisaTTP.SDKHelper.contactlessConfiguration
import com.lovisgod.testVisaTTP.TransactionLogger
import com.lovisgod.testVisaTTP.handlers.Conversions
import com.lovisgod.testVisaTTP.handlers.KeyBoardClick
import com.lovisgod.testVisaTTP.handlers.PPSEManager
import com.lovisgod.testVisaTTP.handlers.PinKeyPadHandler
import com.lovisgod.testVisaTTP.handlers.StringManipulator
import com.lovisgod.testVisaTTP.models.datas.EmvPinData
import com.lovisgod.testVisaTTP.models.enums.KeyMode
import com.lovisgod.testVisaTTP.models.enums.KeyType
import com.lovisgod.testVisaTTP.models.uiState.ReadCardStates
import com.visa.app.ttpkernel.ContactlessConfiguration
import com.visa.app.ttpkernel.ContactlessKernel
import com.visa.app.ttpkernel.TtpOutcome
import com.visa.app.ttpkernel.Version
import com.visa.vac.tc.emvconverter.Utils
import java.io.IOException
import java.util.Arrays

class EmvPaymentHandler : TransactionLogger, KeyBoardClick {

    private var readCardStates: ReadCardStates?  = null
    var context: Context? = null
    var contactlessKernel : ContactlessKernel? = null
    private var  isKimono: Boolean = false
    var DF03 = ""
    var clearPinText = ""
    var continueTransaction  = false
    var dialog: Dialog? = null
    var transactionContactlessResult: HashMap<String, ByteArray>? = null
    val supportedAid = ArrayList(
        Arrays.asList(
            "A0000000031010"
        )
    )

    fun initialize(context: Context) {

    }

    fun setIsKimono(isKimono: Boolean) {
       this.continueTransaction = false
       this.isKimono = isKimono
    }

    fun pay (amount: String, readCardStates: ReadCardStates, context: Context) {
        this.context = context
        contactlessKernel = ContactlessKernel.getInstance(context)
        this.readCardStates = readCardStates
        doTransaction(amount)
    }

    fun continueTransaction(condition: Boolean) {
        this.continueTransaction = true
    }

    fun stopTransaction() {
        stopEmvProcess()
    }

    private fun stopEmvProcess() {
        println("this is called called called")
        try {
            SDKHelper.nfcListener?.resetNFCField()
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    override fun log(message: String) {
        println(message)
    }

    override fun onNumClick(num: String) {
        if (clearPinText.length > 6) {
            Toast.makeText(context as Context, "Pin Cannot be more than 6", Toast.LENGTH_LONG).show()
        } else {
            clearPinText += num
            this.readCardStates?.onPinText("*".repeat(clearPinText.length))
//            view?.findViewById<TextView>(R.id.pin_text)?.text = "*".repeat(clearPinText.length)
        }
    }

    override fun onSubmitButtonClick() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
        println("pin is ${clearPinText}")
        // calculate pin block based on the mode of pin
        if (clearPinText.length < 4) {
            Toast.makeText(context as Context, "Pin not complete", Toast.LENGTH_LONG).show()
        } else {
//            view?.visibility = View.GONE
            val track2  =  transactionContactlessResult?.get("57")?.let { Conversions.parseBERTLV(it)?.find(BerTag("57"))?.getHexValue() }
            println("track 2::::  $track2")
            val pan  = track2?.uppercase()?.split("D")?.get(0)

            println(pan)

            val pinBlock: EmvPinData = if (SDKHelper.getPinMode() == KeyMode.ISO_0) {
                 EmvPinData(
                     ksn = "",
                     CardPinBlock = SDKHelper.getPinBlock(clearPinText, pan.toString(), context as Context)
                 )
            } else {
                val ksnCount: String = SDKHelper.getNextKsnCounter()
                val ksnString = SDKHelper.getKey(KeyType.KSN, this.context!!) + ksnCount
                // calculate the dukpt pinblock here
                val pinBlock = GetPinBlock(
                    SDKHelper.getKey(KeyType.IPEK, this.context!!),
                    ksnString,
                    clearPinText,
                    pan.toString()
                )
                println("dukpt pin block::::: $pinBlock")
                EmvPinData(
                    ksn = StringManipulator.dropFirstCharacter(ksnString, 4),
                    CardPinBlock = pinBlock
                )
            }

            // create transaction data
            transactionContactlessResult?.let {
                val iccData = SDKHelper.getTransactionData(it, pinBlock)
//                runBlocking {
//                    if (iccData != null) {
//                        networkSampleRepo.testTrans(applicationContext, iccData)
//                    }
//                }
                var responseEntity = iccData?.let { it1 ->
                    this@EmvPaymentHandler.readCardStates?.sendTransactionOnline(
                        it1
                    )
                }
            }

            // reset nfc field
            SDKHelper.nfcListener?.resetNFCField()
        }
    }

    override fun onBackSpace() {
        if (clearPinText.isNotEmpty()) {
            clearPinText = clearPinText.substring(0, clearPinText.length -1)
            this.readCardStates?.onPinText("*".repeat(clearPinText.length))
//            view?.findViewById<TextView>(R.id.pin_text)?.text = "*".repeat(clearPinText.length)
        }
    }

    override fun onClear() {
        clearPinText = ""
        this.readCardStates?.onPinText("*".repeat(clearPinText.length))
//        view?.findViewById<TextView>(R.id.pin_text)?.text = "*".repeat(clearPinText.length)
    }



    @SuppressLint("SetTextI18n")
    fun doTransaction(amount: String) {
        val kernelVersion = Version.getVersion()
        println("Kernel version is ${Utils.getHexString(
            kernelVersion
        )}")
        // Select PPSE
        var selectedAid: ByteArray? = null
        val ppseManager = PPSEManager()
        try {
            // Set suppported AID
            ppseManager.setSupportedApps(supportedAid)

            // Perform Select PPSE
            selectedAid = ppseManager.selectPPSE(SDKHelper.newNfcTransceiver)
            println("selected aid === $selectedAid")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (selectedAid != null) {
            // find way to get the card pan
            this@EmvPaymentHandler.readCardStates?.onCardRead("Visa", "")
            val selectedAccountType = this@EmvPaymentHandler.readCardStates?.onSelectAccountType()

            if (selectedAccountType != null) {
                while (continueTransaction) {
                    // Get the ContactlessConfiguration instance
                    contactlessConfiguration = ContactlessConfiguration.getInstance()

                    // Specify the terminal settings for this transaction
                    val myData = contactlessConfiguration?.terminalData
                    myData?.set("9F02", "100".encodeToByteArray())
                    myData?.set("9C", byteArrayOf(0x00))
                    myData?.set("4F", selectedAid) // set the selected aid
                    myData?.set("9F4E", byteArrayOf(
                        0x00.toByte(),
                        0x11.toByte(),
                        0x22.toByte(),
                        0x33.toByte(),
                        0x44.toByte(),
                        0x55.toByte(),
                        0x66.toByte(),
                        0x77.toByte(),
                        0x88.toByte(),
                        0x99.toByte(),
                        0xAA.toByte(),
                        0xBB.toByte(),
                        0xCC.toByte(),
                        0xDD.toByte(),
                        0xEE.toByte(),
                        0xFF.toByte()
                    )
                    )
                    myData?.set("009C", byteArrayOf(0x20.toByte()))

//                    this@EmvPaymentHandler.readCardStates?.onCardDetected()

                    // Call TTP Kernel performTransaction
                    val contactlessResult =
                        contactlessKernel!!.performTransaction(SDKHelper.newNfcTransceiver, contactlessConfiguration)

                    // Check the transaction outcome
                    val outcome = contactlessResult.finalOutcome

                    when (outcome) {
                        TtpOutcome.COMPLETED -> {
                            this.log("Online Approval requested")
                            // Display the TTP Kernel version
                            this.log("ContactlessResult.getKernelData: getVersion")
                            contactlessKernel!!.kernelData

                            // Display the TTP Kernel data
                            val version = contactlessKernel!!.kernelData
                            var key: String
                            var value: String
                            var src: String
                            for ((key1, value1)  in version) {
                                if (value1 != null) {
                                    key = key1 as String
                                    value = Utils.getHexString(value1) as String
                                    this.log("$key:$value")
                                }
                            }
                            // Display the transaction results
                            this.log("""
                                     TLV data format 
                                     card data
                                     ContactlessResult.getData:
                            """.trimIndent())
                            val cardData = contactlessResult.data
                            transactionContactlessResult = contactlessResult.data
                            for ((key1, value1) in cardData) {
                                key = key1 as String
                                if (value1 != null) {
                                    value = Utils.getHexString(value1 as ByteArray?) as String
                                    this.log("""
                                        $key:$value
                                     """.trimIndent())
                                }
                            }

                            this.log(" ContactlessResult.getInternalData():")

                            val internalData = contactlessResult.internalData
                            for ((key1, value1) in internalData) {
                                if (value1 != null) {
                                    key = key1 as String
                                    value = Utils.getHexString(value1) as String
                                    this.log("$key:$value")
                                    //'DF01' Reader CVM Required Limit
                                    //'DF03' CVM Kernel Outcome
                                    //	'00' = CVM capture not required
                                    //	'01' = Signature required
                                    //	'02' = Online PIN required
                                    //	'03' = CVM processing resulted in transaction decline
                                }
                            }



                            // Display the last APDU sent & the last SW received by the TTP Kernel
                            if (contactlessResult.lastApdu != null && contactlessResult.lastSW != null) {
                                this.log("Last APDU Command")
                                this.log(Utils.getHexString(contactlessResult.lastApdu))
                                this.log("Last APDU Response")
                                this.log(Utils.getHexString(contactlessResult.lastSW))
                            }
                            val df03tlv = internalData["DF03"]?.let { Conversions.parseBERTLV(it) }

                            println(df03tlv?.find(BerTag("DF03"))?.getHexValue())

                            DF03 = df03tlv?.find(BerTag("DF03"))?.getHexValue().toString()

                            println("CVM kernel outcome ::: $DF03")

                            if (DF03.isNotEmpty() && (DF03 == "02")) {
                                this@EmvPaymentHandler.readCardStates?.onPinInput()
                                // display pin pad
                                val inflater = LayoutInflater.from(this.context)
                                val view =
                                    inflater.inflate(com.lovisgod.testVisaTTP.R.layout.keypad, null) as ConstraintLayout


                                val keyBoardView = view.findViewById<View>(com.lovisgod.testVisaTTP.R.id.layoutKeyboard)


                                dialog = Dialog(this.context!!, R.style.Theme_Translucent_NoTitleBar)
                                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog!!.setContentView(view)
                                val window: Window? = dialog!!.window
                                val wlp = window?.attributes

                                wlp?.gravity = Gravity.BOTTOM
                                wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
                                window?.setAttributes(wlp)
                                dialog?.window?.setLayout(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                PinKeyPadHandler.handleKeyButtonClick(this, view)
                                window?.setGravity(Gravity.BOTTOM)
                                this.continueTransaction = false
                                dialog?.show()

                            }else if (DF03.isNotEmpty() && (DF03 == "00")) {
                                transactionContactlessResult?.let {
                                    val iccData = SDKHelper.getTransactionData(it, EmvPinData(ksn = "", CardPinBlock = ""))
                                    var responseEntity = iccData?.let { it1 ->
                                        this@EmvPaymentHandler.readCardStates?.sendTransactionOnline(
                                            it1
                                        )
                                    }
                                }

                            }
                            else if (DF03.isNotEmpty() && (DF03 == "01")) {
                                transactionContactlessResult?.let {
                                    val iccData = SDKHelper.getTransactionData(it, EmvPinData(ksn = "", CardPinBlock = ""))
                                    var responseEntity = iccData?.let { it1 ->
                                        this@EmvPaymentHandler.readCardStates?.sendTransactionOnline(
                                            it1
                                        )
                                    }
                                }
                            } else {
                                println("this is the cvm result :::: $DF03")
                                // declined
                                this.readCardStates?.onTransactionFailed("Transaction decline with cvm decline.")
                            }

                        }

                        TtpOutcome.DECLINED -> {
                            this.log("Transaction Declined")
                            this.readCardStates?.onTransactionFailed("Transaction declined offline. Kindly try again with another interface")
                        }

                        TtpOutcome.ABORTED -> {
                            this.log("Transaction terminated")
                            this.readCardStates?.onTransactionFailed("Transaction terminated. Kindly try again with another interface")
                        }

                        TtpOutcome.TRYNEXT ->{
                            this.log("PPSE:Try Next Application")
                            // Check TTP Kernel transaction outcome
                            selectedAid = if (outcome == TtpOutcome.TRYNEXT) {
                                // Perform another transaction using next AID in the list
                                ppseManager.nextCandidate()
                            } else {
                                null
                            }
                        }

                        TtpOutcome.SELECTAGAIN -> {
                            this.log("GPO Returned 6986. Application Try Again.")
                            this.readCardStates?.onTransactionFailed("Transaction declined. Kindly try again with another interface")
                        }

                        else -> {}
                    }

                }

            }

        }
    }

}